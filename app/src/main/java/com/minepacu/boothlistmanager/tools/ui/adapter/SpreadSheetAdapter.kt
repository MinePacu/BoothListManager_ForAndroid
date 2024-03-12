package com.minepacu.boothlistmanager.tools.ui.adapter

import android.widget.TextView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.tools.ReadSheet.ReadSpreadsheetContract

class SpreadSheetAdapter(val items : MutableList<BoothInformation>) : RecyclerView.Adapter<SpreadSheetAdapter.> {

    override fun getItemCount(): Int = items.size

    class RowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    }
}