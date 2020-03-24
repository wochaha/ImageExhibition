package com.example.imageexhibition.util

import com.example.imageexhibition.model.ImageAndTextModel
import com.example.imageexhibition.model.ResultWrapper
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("News/new_list")
    suspend fun getInfo(@QueryMap params:Map<String,String>):ResultWrapper
}