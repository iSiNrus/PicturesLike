package ru.barsik.pictureslike.di.module

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.barsik.pictureslike.BuildConfig
import ru.barsik.pictureslike.di.scope.ApiScope
import ru.barsik.pictureslike.repo.server.ApiService
import ru.barsik.pictureslike.repo.server.ServerCommunicator
import java.util.concurrent.TimeUnit

@Module
class ApiModule {

    companion object{
        private val BASE_URL = "https://picsum.photos"
    }

    @Provides
    @ApiScope
    fun providesCommunicator(apiService: ApiService): ServerCommunicator {
        return ServerCommunicator(apiService)
    }

    @Provides
    @ApiScope
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create<ApiService>(ApiService::class.java)
    }

    @Provides
    @ApiScope
    fun providesRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(BASE_URL).build()
    }

    @Provides
    @ApiScope
    fun providesRetrofitBuilder() : Retrofit.Builder{

        val builder = OkHttpClient.Builder()
            .connectionPool(ConnectionPool(5, 90, TimeUnit.SECONDS))
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(Interceptor {
                val request: Request = it.request()
                var response = it.proceed(request)
                var tryCount: Int = 0
                while (!response.isSuccessful && tryCount<3){
                    Log.d("intercept", "Request is not successful - $tryCount");
                    tryCount++
                    response.close()
                    response = it.proceed(request)
                }
                return@Interceptor(response)
            })
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)
        }

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }
}