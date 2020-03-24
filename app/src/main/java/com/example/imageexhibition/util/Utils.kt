package com.example.imageexhibition.util

import android.content.res.Resources
import android.util.DisplayMetrics

fun getScreenWidth():Float = Resources.getSystem().displayMetrics.widthPixels.toFloat()

fun getScreenHeight():Float = Resources.getSystem().displayMetrics.heightPixels.toFloat()