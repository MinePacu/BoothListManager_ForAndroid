package com.minepacu.boothlistmanager.ui.home

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaquo.python.PyException
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.Result
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
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

    private var _image_login = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_login: LiveData<Int> = _image_login

    var isLoginToGoogleAPI = false
    var isLoadedSheetId = false

    fun loginToGoogleAPI(view : View, buttonReloadSheetInfo: Button) {
        viewModelScope.launch {
            val result = try {
                PythonClass.loginToGoogleAPI()
            } catch (e : PyException) {
                Result.Error(Exception(e.message))
            }

            when (result) {
                is Result.Success<Boolean> -> {
                    _text_ServiceConnectionStatus.value = "로그인 됨"
                    isLoginToGoogleAPI = true
                    _image_login.value = R.drawable.check_circle_24dp
                    buttonReloadSheetInfo.isEnabled = true
                }
                else -> {
                    _text_ServiceConnectionStatus.value = "로그인 불가"
                    _image_login.value = R.drawable.cancel_24dp
                    Snackbar.make(view, "구글 API에 로그인할 수 없습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun getSheet(view : View, sheetId: String) {
        viewModelScope.launch {
            val result2 = try {
                PythonClass.getSheetInfo(sheetId)
            } catch (e : PyException) {
                Result.Error(Exception(e.message))
            }

            when (result2) {
                is Result.Success<Boolean> -> {
                    isLoadedSheetId = true
                    _text_sheetTitle.value =
                        PythonClass.sheetInfo?.get("title").toString()
                }
                else -> {
                    _text_sheetTitle.value = "시트 없음"
                    Snackbar.make(view, "시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}