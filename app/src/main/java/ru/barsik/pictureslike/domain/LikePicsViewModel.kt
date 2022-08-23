package ru.barsik.pictureslike.domain

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.barsik.pictureslike.repo.AppRepository
import ru.barsik.pictureslike.repo.model.PictureEntity

class LikePicsViewModel(app: Application, private val repository: AppRepository) :
    AndroidViewModel(app) {

    private val TAG = "LikePicsViewModel"

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
}