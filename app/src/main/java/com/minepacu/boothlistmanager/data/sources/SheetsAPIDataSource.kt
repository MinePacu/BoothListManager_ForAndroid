package com.minepacu.boothlistmanager.data.sources

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.tools.AuthenticationManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class SheetsAPIDataSource(private val authManager: AuthenticationManager,
                          private val transport: HttpTransport,
                          private val jsonFactory: JsonFactory) : SheetsDataSource {
    private val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport, jsonFactory, authManager.googleAccountCredential)
                .setApplicationName("test")
                .build()
        }

    override fun readSpreadSheet(
        spreadsheetId: String,
        spreadSheetRange: String
    ): Single<List<BoothInformation>> {
        return Observable
            .fromCallable{
                val respone = sheetsAPI.spreadsheets().values()
                                 .get(spreadsheetId, spreadSheetRange)
                                 .execute()
                respone.getValues() }
            .flatMapIterable { it -> it }
            .map {  BoothInformation(it[0].toString(), it[1].toString(),
                                    it[2].toString(), it[3].toString(),
                                    it[4].toString(), it[5].toString(),
                                    it[6].toString(), it[7].toString(), it[8].toString()) }
            .toList()
        }
}