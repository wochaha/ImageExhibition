package com.example.imageexhibition.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ImageAndTextModel(
    @SerializedName("title") val title:String,
    @SerializedName("imgsrc") val imageSrc:String)