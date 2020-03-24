package com.example.imageexhibition.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServer {
    private const val DEFAULT_TIME_OUT = 10L

    private var retrofit: Retrofit
    private var okHttpClient: OkHttpClient

    init {
        okHttpClient = OkHttpClient()
            .newBuilder()
            .connectTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> getApiService(clazz: Class<T>) = retrofit.create(clazz)
}