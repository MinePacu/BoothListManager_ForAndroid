package com.minepacu.boothlistmanager.ui.ProgressingPage

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.minepacu.boothlistmanager.R

class ProgressPage(context : Context) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.page_progress)
    }
}