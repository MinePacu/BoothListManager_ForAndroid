package com.minepacu.boothlistmanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minepacu.boothlistmanager.data.model.Result
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var _text_ServiceConnectionStatus = MutableLiveData<String>().apply {
        value = "서비스 상태"
    }
    var text_ServiceConnectionStatus: LiveData<String> = _text_ServiceConnectionStatus

    private var _text_sheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_sheetTitle: LiveData<String> = _text_sheetTitle

    private val pythonClass = PythonClass()

    fun loginToGoogleAPI() {
        viewModelScope.launch {
            val result = pythonClass.loginToGoogleAPI()

            when (result) {
                is Result.Success<Boolean> -> _text_ServiceConnectionStatus.value = "로그인 됨"
                else -> _text_ServiceConnectionStatus.value = "로그인 불가"
            }
        }
    }

    fun getSheet() {
        viewModelScope.launch {
            val result2 = pythonClass.getSheetInfo()

            when (result2) {
                is Result.Success<Boolean> -> _text_sheetTitle.value =
                    pythonClass.sheetInfo?.get("title").toString()
                else -> _text_sheetTitle.value = "시트 없음"
            }
        }
    }
}