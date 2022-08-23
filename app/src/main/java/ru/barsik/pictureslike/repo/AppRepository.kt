package ru.barsik.pictureslike.repo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import ru.barsik.pictureslike.repo.model.PictureEntity
import ru.barsik.pictureslike.repo.server.ServerCommunicator
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AppRepository(
    private val serverCommunicator: ServerCommunicator,
    private val db: AppDatabase
) {
    private val TAG = "AppRepository"
//    private var allSavedPictures : ArrayList<Pair<PictureEntity, Bitmap>>? = null

    fun getImage200() : Single<ResponseBody>{
        return serverCommunicator.getImage200()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getPagePictures(page: Int, limit: Int = 10): Single<List<PictureEntity>>{
        return serverCommunicator.getPagePictures(page, limit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getImagesByEntity(listPics: List<PictureEntity>): Observable<ResponseBody>{
        return serverCommunicator.getImagesByEntity(listPics)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getAllSavedPictures(): Single<List<Pair<PictureEntity, Bitmap>>> {
        return Single.create { emitter ->
            db.pictureEntityDAO().readAllSavedPictures()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val res = ArrayList<Pair<PictureEntity, Bitmap>>()
                    Log.d(TAG, "readAllSavedPictures: ${it.size}")
                    for (pe in it) {
                        res.add(Pair(pe, getImageFromFile(pe)))
                    }
                    emitter.onSuccess(res)
                }, {
                    Log.d(TAG, "readAllSavedPictures: ${it.message}")
                })
        }
    }

    private fun getImageFromFile(pictureEntity: PictureEntity) : Bitmap {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val fname = "piclike_${pictureEntity.id}_${pictureEntity.author}.jpg"

        val file = File(myDir, fname)
        return if (file.exists()){
            try {
                val input = FileInputStream(file)
                val bitmap = BitmapFactory.decodeStream(input)
                input.close()
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                Bitmap.createBitmap(intArrayOf(200, 100, 100), 300, 0, 300, 300, Bitmap.Config.RGB_565)
            }
        } else {
            Bitmap.createBitmap(intArrayOf(200, 100, 100), 0, 300, 300, 300, Bitmap.Config.RGB_565)
        }
    }

    fun savePicture(pictureEntity: PictureEntity, bitmap: Bitmap){
        db.pictureEntityDAO().savePictureEntity(pictureEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                Log.d(TAG, "savePicture: $it")
            },{})

        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        Log.d(TAG, "savePicture: ${myDir.absolutePath}")
        val fname = "piclike_${pictureEntity.id}_${pictureEntity.author}.jpg"

        val file = File(myDir, fname)
        Log.d(TAG, "savePicture: ${file.absolutePath}")
        try {
            file.createNewFile()
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deletePicture(pictureEntity: PictureEntity){
        db.pictureEntityDAO().deletePictureById(pictureEntity.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                Log.d(TAG, "deletePicture: $it")
                if(it==1) {
                    val root =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .toString()
                    val myDir = File("$root/saved_images")
                    myDir.mkdirs()

                    val fname = "piclike_${pictureEntity.id}_${pictureEntity.author}.jpg"

                    val file = File(myDir, fname)
                    file.delete()
                }
            },{})


    }

}