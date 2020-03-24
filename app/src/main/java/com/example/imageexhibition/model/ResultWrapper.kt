package com.example.imageexhibition.model

import com.google.gson.annotations.SerializedName

data class ResultWrapper(
    @SerializedName("msg") val msg:String,
    @SerializedName("code") val code : Int,
    @SerializedName("data") val data:MutableList<ImageAndTextModel>
)