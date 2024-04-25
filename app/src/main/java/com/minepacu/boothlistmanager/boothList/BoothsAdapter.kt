package com.minepacu.boothlistmanager.boothList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.R

class BoothsAdapter(private val onClick: (BoothInfo) -> Unit) :
    ListAdapter<BoothInfo, BoothsAdapter.BoothViewHolder>(BoothDiffCallback){
    class BoothViewHolder(itemView: View, val onClick: (BoothInfo) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
            private val boothHeaderTextView: TextView = itemView.findViewById(R.id.boothNumber_Title_list)
            private val boothSubTitleTextView: TextView = itemView.findViewById(R.id.boothGenre_list)
            private var currentBooth: BoothInfo? = null

            init {
                itemView.setOnClickListener {
                    currentBooth?.let {
                        onClick(it)
                    }
                }
            }

            fun bind(boothinfo: BoothInfo) {
                currentBooth = boothinfo

                val title_string = "${boothinfo.boothnumber}: ${boothinfo.boothname}"
                val subtitle_string = "장르: ${boothinfo.genres}"

                boothHeaderTextView.text = title_string
                boothSubTitleTextView.text = subtitle_string
            }
        }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int): BoothViewHolder {
        val view = LayoutInflater.from(group.context).inflate(R.layout.booth_item, group, false)
        return BoothViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: BoothViewHolder, position: Int) {
        val boothinfo = getItem(position)
        holder.bind(boothinfo)
    }
}

object BoothDiffCallback: DiffUtil.ItemCallback<BoothInfo>() {
    override fun areItemsTheSame(oldItem: BoothInfo, newItem: BoothInfo): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: BoothInfo, newItem: BoothInfo): Boolean {
        return oldItem.boothname == newItem.boothname
    }
}