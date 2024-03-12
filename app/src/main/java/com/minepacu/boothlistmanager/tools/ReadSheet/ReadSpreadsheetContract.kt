package com.minepacu.boothlistmanager.tools.ReadSheet

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.minepacu.boothlistmanager.data.model.BoothInformation
import com.minepacu.boothlistmanager.tools.bases.BasePresenter
import com.minepacu.boothlistmanager.tools.bases.BaseView

interface ReadSpreadsheetContract {
    interface View : BaseView {
        fun initList(booth: MutableList<BoothInformation>)
        fun showBooth()
        fun showUserName(username : String)
        fun _startAuthentication(client : GoogleSignInClient)
    }

    interface Presenter : BasePresenter {
        fun startAuthentication()
        fun loginSuccessful()
        fun loginFailed()

    }
}