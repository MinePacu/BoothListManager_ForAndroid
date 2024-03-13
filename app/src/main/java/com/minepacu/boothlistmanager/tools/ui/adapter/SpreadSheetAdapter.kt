package com.minepacu.boothlistmanager.tools.ui.adapter

import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.R

class SpreadSheetAdapter(val items : MutableList<BoothInformation>) : RecyclerView.Adapter<SpreadSheetAdapter.RowViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val currentBooth = items.get(position)
        holder.setData(currentBooth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        return RowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_row, parent, false))
    }

    class RowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val tvBoothNumber : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_boothnumber) }
        private val tvBoothName : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_boothname) }
        private val tvGenre : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_genre) }
        private val tvYoil : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_yoil) }
        private val tvInfo : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_info) }
        private val tvPreOrderDate : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_preorderDate) }
        private val tvPreOrderLink : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_preorderLink) }

        fun setData(boothInformation: BoothInformation) {
            tvBoothNumber.text = boothInformation.BoothNumber
            tvBoothName.text = boothInformation.BoothName
            tvGenre.text = boothInformation.Genre
            tvYoil.text = boothInformation.Yoil
            tvInfo.text = boothInformation.InfoLink
            tvPreOrderDate.text = boothInformation.PreOrderDate
            tvPreOrderLink.text = boothInformation.PreOrderLink
        }
    }
}