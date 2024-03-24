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

    private var _text_worksheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_worksheetTitle: LiveData<String> = _text_worksheetTitle

    private var _image_login = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_login: LiveData<Int> = _image_login

    private var _image_loadedsheetInfo = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_loadedsheetInfo: LiveData<Int> = _image_loadedsheetInfo

    private var _image_loadedworksheetInfo = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_loadedworksheetInfo: LiveData<Int> = _image_loadedworksheetInfo

    var isLoginToGoogleAPI = false
    var isLoadedSheetId = false

    fun loginToGoogleAPI(view : View, buttonReloadSheetInfo: Button, buttonReloadWorkSheetInfo: Button) {
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
                    buttonReloadWorkSheetInfo.isEnabled = true
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

    fun getSheet(view : View, sheetId: String, sheetNumber: Int) {
        viewModelScope.launch {
            val result2 = try {
                PythonClass.getSheet_WorkSheet(sheetId, sheetNumber)
            } catch (e : PyException) {
                Result.Error(Exception(e.message))
            }

            when (result2) {
                is Result.Success<Boolean> -> {
                    isLoadedSheetId = true
                    _text_sheetTitle.value =
                        PythonClass.getVariable("sheet")?.get("title").toString()
                    _image_loadedsheetInfo.value = R.drawable.check_circle_24dp
                }
                else -> {
                    _text_sheetTitle.value = "시트 없음"
                    _image_loadedsheetInfo.value = R.drawable.cancel_24dp
                    Snackbar.make(view, "시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                        .show()
                }
            }

            getWorksheetTitle(view, isLoadedSheetId)
        }
    }

    fun getWorksheetTitle(view: View, isLoadedSheetInfo: Boolean) {
        viewModelScope.launch {
            if (isLoadedSheetInfo == false) {
                _text_worksheetTitle.value = "없음"
                _image_loadedworksheetInfo.value = R.drawable.cancel_24dp
                Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                val result = try {
                    PythonClass.getVariable("worksheet")?.get("title").toString()
                } catch (e: PyException) {
                    "null"
                }

                when {
                    result != "null" -> {
                        _text_worksheetTitle.value = result
                        _image_loadedworksheetInfo.value = R.drawable.check_circle_24dp
                    }

                    else -> {
                        _text_worksheetTitle.value = "없음"
                        _image_loadedworksheetInfo.value = R.drawable.cancel_24dp
                        Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}