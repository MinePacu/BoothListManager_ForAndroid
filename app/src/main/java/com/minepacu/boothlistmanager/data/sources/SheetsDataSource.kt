package com.minepacu.boothlistmanager.data.sources

import com.minepacu.boothlistmanager.data.model.BoothInformation
import io.reactivex.rxjava3.core.Single

interface SheetsDataSource {
    fun readSpreadSheet(spreadsheetId : String,
                        spreadSheetRange : String) : Single<List<BoothInformation>>
}