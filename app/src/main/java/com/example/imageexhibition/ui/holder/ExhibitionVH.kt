package com.example.imageexhibition.ui.holder

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageexhibition.R
import com.example.imageexhibition.model.ImageAndTextModel
import com.example.imageexhibition.ui.detail.EntryActivity
import com.example.imageexhibition.util.fix
import kotlinx.android.synthetic.main.item_exhibition.view.*

class ExhibitionVH(view:View) : RecyclerView.ViewHolder(view) {

    fun bind(info:ImageAndTextModel,width:Int){
        val TAG = "ExhibitionVH"

        itemView.exhibition_image.fix(width,width)

        //url不是https所以需要启用http可访问权限
        Glide.with(itemView.exhibition_image)
            .load(info.imageSrc)
            .placeholder(R.drawable.ic_launcher_foreground)
            .override(width,width)
            .into(itemView.exhibition_image)

        Log.d(TAG,"imgSrc: ${info.imageSrc} description: ${info.title}")

        itemView.image_description.text = info.title

        itemView.setOnClickListener {
            val intent = Intent(itemView.context,EntryActivity::class.java)
            intent.putExtra("title",info.title)
            intent.putExtra("url",info.imageSrc)
            itemView.context.startActivity(intent)
        }
    }
}