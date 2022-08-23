package ru.barsik.pictureslike.repo.server

import android.util.Log
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import ru.barsik.pictureslike.repo.model.PictureEntity
import java.util.*
import kotlin.collections.ArrayList

class ServerCommunicator(private val mService: ApiService) {

    private val TAG = "ServerCommunicator"

    fun getImage200(): Single<ResponseBody> {
        return mService.getImage()
    }

    fun getPagePictures(page: Int, limit: Int): Single<List<PictureEntity>> {
        return mService.getPagePictures(page, limit)
    }

    fun getImagesByEntity(listPics : List<PictureEntity>): Observable<ResponseBody> {
        val requestList = ArrayList<Observable<ResponseBody>>()
        listPics.forEach { item ->
            Log.d(TAG, "getImagesByEntity: ${item.id} ${item.width} ${item.height}")
            requestList.add(mService.getImageByPath(item.id ?: "", item.width, item.height).retry(5))
        }
        return Observable.concat(requestList)
    }
}