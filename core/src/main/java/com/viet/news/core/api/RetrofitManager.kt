package com.viet.news.core.api

import com.viet.news.core.BuildConfig
import com.viet.news.core.http.interceptor.HeaderInterceptor
import com.viet.news.core.http.interceptor.LoggingInterceptor
import com.viet.news.core.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor() {

    private val apiService: ApiService

    private val okHttpClient: OkHttpClient

    init {
        val loggingInterceptor = LoggingInterceptor.Builder()
                .loggable(true)
                .request()
                .requestTag("Request")
                .response()
                .responseTag("Response")
                .loggable(BuildConfig.DEBUG)

        okHttpClient = OkHttpClient.Builder()
                .writeTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .connectTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(HeaderInterceptor())
                .addInterceptor(loggingInterceptor.build())
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(ApiService.MAGICBOX_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun retrofit(): Retrofit = retrofit

    fun apiService(): ApiService = apiService

    fun okHttpClient(): OkHttpClient = okHttpClient

    private object Holder {
        val MANAGER = RetrofitManager()
    }

    companion object {

        private lateinit var retrofit: Retrofit

        @JvmStatic
        fun get(): RetrofitManager {
            return Holder.MANAGER
        }
    }
}