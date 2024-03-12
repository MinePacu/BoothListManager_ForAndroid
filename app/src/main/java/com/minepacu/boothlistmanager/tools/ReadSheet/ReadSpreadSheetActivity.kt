package com.minepacu.boothlistmanager.tools.ReadSheet

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minepacu.boothlistmanager.tools.bases.BaseActibity

class ReadSpreadSheetActivity :
    BaseActibity<ReadSpreadsheetContract.Presenter>(), ReadSpreadsheetContract.View {

        private lateinit var tvBoothName : TextView
        private lateinit var rvSpreadSheet : RecyclerView
}