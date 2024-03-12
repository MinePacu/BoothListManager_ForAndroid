package com.minepacu.boothlistmanager.tools.ReadSheet

import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.data.sources.SheetRepository
import com.minepacu.boothlistmanager.tools.AuthenticationManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class ReadSpreadSheetPresenter(private val view: ReadSpreadsheetContract.View,
                               private val authenticationManager: AuthenticationManager,
                               private val sheetsRepository: SheetRepository)
    : ReadSpreadsheetContract.Presenter {

        private lateinit var readSpreadsheetDisposable : Disposable
        private val booths : MutableList<BoothInformation> = mutableListOf()

    override fun startAuthentication() {
        view._startAuthentication(authenticationManager.googleSignInClient)
    }

    override fun init() {
        startAuthentication()
        view.initList(booths)
    }

    override fun dispose() {
        readSpreadsheetDisposable.dispose()
    }

    override fun loginSuccessful() {
        view.showUserName(authenticationManager.getLastSignedAccount()?.displayName!!)
        authenticationManager.setUpGoogleAccountCredential()

    }

    override fun loginFailed() {
        TODO("Not yet implemented")
    }

    private fun startReadingSpreadSheet(spreadsheetId : String, range : String) {
        booths.clear()
        readSpreadsheetDisposable =
            sheetsRepository.readSpreadSheet(spreadsheetId, range)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.ShowError(it.message!!)}
                .subscribe(Consumer {
                    booths.addAll(it)
                    view.showBooth()
                })
    }

    companion object {
        val spreadsheetId = "Sample"
        val range = "Range"
    }

}