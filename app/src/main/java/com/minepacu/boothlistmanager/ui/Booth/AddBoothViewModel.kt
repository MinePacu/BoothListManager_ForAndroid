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

    /**
     * 지정한 워크 시트에 부스 정보를 추가합니다.
     *
     * 지정한 워크 시트는 [PythonClass.boothListManagementModule]에 있는 sheetNumber에 의해 결정됩니다.
     *
     * @param view 오류가 났을 떄, 오류 메시지를 출력할 [Snackbar]를 사용할 [View]
     * @param progressPage [android.app.Dialog.show] 함수를 이미 사용한 [ProgressPage] 객체
     * @param boothInfo 추가할 부스의 정보가 저장된 [BoothInfo] 객체
     *
     * @return 부스 추가 작업을 수행하는 [Job] 객체
     */
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