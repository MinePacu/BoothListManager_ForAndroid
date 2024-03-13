package com.minepacu.boothlistmanager.tools.ui.read

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.data.sources.SheetRepository
import com.minepacu.boothlistmanager.data.sources.SheetsAPIDataSource
import com.minepacu.boothlistmanager.tools.AuthenticationManager
import com.minepacu.boothlistmanager.tools.ReadSheet.ReadSpreadSheetPresenter
import com.minepacu.boothlistmanager.tools.ReadSheet.ReadSpreadsheetContract
import com.minepacu.boothlistmanager.tools.bases.BaseActibity
import com.minepacu.boothlistmanager.tools.ui.adapter.SpreadSheetAdapter
import java.util.Arrays

class ReadSpreadSheetActivity :
    BaseActibity<ReadSpreadsheetContract.Presenter>(), ReadSpreadsheetContract.View {
    private lateinit var rvSpreadSheet : RecyclerView

    private lateinit var spreadSheetAdapter: SpreadSheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_spreadsheet)
        bindingViews()
        presenter.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.loginSuccessful()
            } else {
                presenter.loginFailed()
            }
        }
    }

    override fun initDependencies() {
        val signInOptions : GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
                .requestScopes(Drive.SCOPE_FILE)
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        val googleAccountCredential = GoogleAccountCredential
            .usingOAuth2(this, Arrays.asList(*AuthenticationManager.SCOPES))
            .setBackOff(ExponentialBackOff())
        val authManager =
            AuthenticationManager(lazyOf(this), googleSignInClient, googleAccountCredential)
        val sheetsApiDataSource =
            SheetsAPIDataSource(authManager,
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance())
        val sheetsRepository = SheetRepository(sheetsApiDataSource)
        presenter = ReadSpreadSheetPresenter(this, authManager, sheetsRepository)
    }

    private fun bindingViews() {
        rvSpreadSheet = findViewById(R.id.rv_spreadsheet)
    }

    override fun ShowError(error : String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun initList(booth: MutableList<BoothInformation>) {
        spreadSheetAdapter = SpreadSheetAdapter(booth)
        rvSpreadSheet.layoutManager = LinearLayoutManager(this)
        rvSpreadSheet.adapter = spreadSheetAdapter
    }

    override fun showBooth() {
        spreadSheetAdapter.notifyDataSetChanged()
    }

    override fun _startAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, RQ_GOOGLE_SIGN_IN)
    }

    override fun showStatus(status: String) {
        TODO("Not yet implemented")
    }

    companion object {
        const val TAG = "ReadSpreadsheetActivity"
        const val RQ_GOOGLE_SIGN_IN = 999
    }
}