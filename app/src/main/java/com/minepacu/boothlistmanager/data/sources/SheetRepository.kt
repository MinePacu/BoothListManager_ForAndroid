package com.minepacu.boothlistmanager.data.sources

import com.minepacu.boothlistmanager.data.model.BoothInformation
import io.reactivex.rxjava3.core.Single

class SheetRepository(private val sheetsAPIDataSource: SheetsAPIDataSource) {
    fun readSpreadSheet(spreadsheetId : String,
                        spreadsheetRange : String): Single<List<BoothInformation>> {
        return sheetsAPIDataSource.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }
}