package com.example.imageexhibition.ui.detail

import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.imageexhibition.R
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.coroutines.*

class ImageFragment private constructor(): Fragment() {
    private var url:String? = ""
    private val metrics = DisplayMetrics()
    private val TAG = "ImageFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it["url"] as String
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(metrics)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //todo 图像相关操作
        GlobalScope.launch(Dispatchers.Main) {

            val bitmap = withContext(Dispatchers.IO){
                Glide.with(this@ImageFragment)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()
            }

            detail_image.setImageBitmap(bitmap)
        }

    }

    companion object{
        fun newInstance(url:String):ImageFragment{
            val bd = bundleOf(
                "url" to url
            )
            val fragment = ImageFragment()
            fragment.arguments = bd
            return fragment
        }
    }
}