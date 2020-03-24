package com.example.imageexhibition.ui.adapter

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.imageexhibition.R
import com.example.imageexhibition.model.ImageAndTextModel
import com.example.imageexhibition.ui.holder.ExhibitionVH

class ExhibitionAdapter(
    val exhibitions:MutableList<ImageAndTextModel> = mutableListOf(),
    var width:Int = 0
) : RecyclerView.Adapter<ExhibitionVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionVH {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exhibition,parent,false)

        return ExhibitionVH(itemView)
    }

    override fun getItemCount(): Int {
        return exhibitions.size
    }

    override fun onBindViewHolder(holder: ExhibitionVH, position: Int) {
        val info = exhibitions[position]
        holder.bind(info,width)
    }
}