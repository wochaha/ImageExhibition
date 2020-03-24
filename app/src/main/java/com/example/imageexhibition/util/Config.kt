package com.example.imageexhibition.util

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

const val API_URL = "https://api.isoyu.com/api/"

fun View.margin(top:Int,bottom:Int,left:Int,right:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left,top,right,bottom)
        this.requestLayout()
    }
}

fun View.margin(distance:Int) = margin(distance,distance,distance,distance)

fun View.fix(width:Int,height:Int){
    val originLp = this.layoutParams
    val lp = when(originLp){
        is LinearLayout.LayoutParams -> originLp
        is RelativeLayout.LayoutParams -> originLp
        is GridLayoutManager.LayoutParams -> originLp
        else -> originLp as ViewGroup.LayoutParams
    }

    lp.width = width
    lp.height = height
    this.layoutParams = lp
}