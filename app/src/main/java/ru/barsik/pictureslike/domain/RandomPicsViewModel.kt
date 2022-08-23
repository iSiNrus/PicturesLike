package ru.barsik.pictureslike.domain

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import ru.barsik.pictureslike.repo.AppRepository
import ru.barsik.pictureslike.repo.model.PictureEntity
import ru.barsik.pictureslike.repo.server.ServerCommunicator
import kotlin.math.log

class RandomPicsViewModel(app: Application, private val repository: AppRepository) : AndroidViewModel(app) {
    private val TAG = "RandomPicsViewModel"

    private val imageLiveData = MutableLiveData<ArrayList<Bitmap>>(ArrayList<Bitmap>())
    private val imagesPageLiveData = MutableLiveData<ArrayList<PictureEntity>>(ArrayList<PictureEntity>())
    var page: Int = 0
    fun updatePic() {
        repository.getImage200().subscribe({
                val bitmap = BitmapFactory.decodeStream(it.byteStream())
            val list = imageLiveData.value as ArrayList
            list.add(bitmap)
            imageLiveData.postValue(list)
        }, {
            Log.d(TAG, "updatePic: ERROR")
        })
    }

    private val allSavedPicturesLiveData =
        MutableLiveData<List<Pair<PictureEntity, Bitmap>>>(ArrayList())

    fun getAllSavedPictures() {
        repository.getAllSavedPictures()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                allSavedPicturesLiveData.postValue(it)
            }, {
                Log.d(TAG, "getAllSavedPictures: ${it.message}")
            })
    }

    fun getAllSavedPicturesLD() = allSavedPicturesLiveData

    fun downloadPagePictures(){
        page++
        repository.getPagePictures(page).subscribe({ list ->
            val picList = imagesPageLiveData.value!!
            picList.addAll(list)
            imagesPageLiveData.postValue(picList)
            for(pic in list){
                Log.d(TAG, "page $page pic ${pic.id} ${pic.author}")
                pic.height = pic.height/10
                pic.width = pic.width/10
            }

            repository.getImagesByEntity(list).subscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    val btmList = imageLiveData.value!!
                btmList.add(bitmap)
                    imageLiveData.postValue(btmList)
            },{
                Log.d(TAG, "meee: ${it.message}")
                it.printStackTrace()
            })
        },{
            page--
            Log.d(TAG, "updatePic: ERROR ${it.message}")
        })
    }

    fun savePicture(pictureEntity: PictureEntity, bitmap: Bitmap){
        Log.d(TAG, "savePicture: ${pictureEntity.id}")
        repository.savePicture(pictureEntity, bitmap)
    }
    fun deletePicture(pictureEntity: PictureEntity){
        Log.d(TAG, "deletePicture: ${pictureEntity.id}")
        repository.deletePicture(pictureEntity)
    }
    fun getPicLD() = imageLiveData
    fun getImagesPage() = imagesPageLiveData
}