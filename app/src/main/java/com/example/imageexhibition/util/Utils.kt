package com.example.imageexhibition.util

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager

fun View.margin(top:Int, bottom:Int, left:Int, right:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left,top,right,bottom)
        this.requestLayout()
    }
}

fun View.margin(distance:Int) = margin(distance,distance,distance,distance)

fun View.fix(width:Int, height:Int){
    val lp = when(val originLayoutParams = this.layoutParams){
        is LinearLayout.LayoutParams -> originLayoutParams
        is RelativeLayout.LayoutParams -> originLayoutParams
        is GridLayoutManager.LayoutParams -> originLayoutParams
        else -> originLayoutParams as ViewGroup.LayoutParams
    }

    lp.width = width
    lp.height = height
    this.layoutParams = lp
}

fun getScreenWidth():Float = Resources.getSystem().displayMetrics.widthPixels.toFloat()

fun getScreenHeight():Float = Resources.getSystem().displayMetrics.heightPixels.toFloat()