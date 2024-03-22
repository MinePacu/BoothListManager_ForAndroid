package com.minepacu.boothlistmanager.ui.Booth

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.data.model.Result
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddBoothViewModel : ViewModel() {

    fun addBoothInfoToSheet(view : View, progressPage: ProgressPage?, boothInfo : BoothInfo): Job {
        return viewModelScope.launch {
            val result = async {  PythonClass.addBoothInfoToSheet(boothInfo) }
            val result_ = result.await()

            when (result_) {
                is Result.Success<Boolean> -> {
                    Snackbar.make(view, "부스 목록 시트에 해당 부스 정보가 추가되었습니다.",
                        Snackbar.LENGTH_LONG)
                        .show()
                    progressPage?.hide()
                }
                else -> {
                    Snackbar.make(
                        view, "부스 목록 시트에 부스 정보가 추가되지 못했습니다.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                    progressPage?.hide()
                }
            }
        }
    }
}