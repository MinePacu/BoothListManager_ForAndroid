package com.minepacu.boothlistmanager.ui.Booth

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.data.model.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddBoothViewModel : ViewModel() {

    fun addBoothInfoToSheet(view : View, boothInfo : BoothInfo): Job {
        return viewModelScope.launch {
            val result = PythonClass.addBoothInfoToSheet(boothInfo)

            when (result) {
                is Result.Success<Boolean> -> {
                    Snackbar.make(view, "부스 목록 시트에 해당 부스 정보가 추가되었습니다.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> Snackbar.make(view, "부스 목록 시트에 부스 정보가 추가되지 못했습니다.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}