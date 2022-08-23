package ru.barsik.pictureslike.repo.server

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.barsik.pictureslike.repo.model.PictureEntity

interface ApiService {

    @GET("/500")
    fun getImage() : Single<ResponseBody>

    @GET("/v2/list")
    fun getPagePictures(
        @Query("page") page: Int,
        @Query("limit") limit : Int
    ) : Single<List<PictureEntity>>

    @GET("/v2/list")
    fun getPagePicObs(
        @Query("page") page: Int,
        @Query("limit") limit : Int
    ) : Observable<List<PictureEntity>>

    @GET("/id/{identif}/{width}/{height}")
    fun getImageByPath(
        @Path("identif") id: String,
        @Path("width") width : Int,
        @Path("height") height : Int
    ) : Observable<ResponseBody>

}