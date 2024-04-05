package com.minepacu.boothlistmanager.ui.home

import android.util.Log
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

    // Google API Status TextView
    private var _text_ServiceConnectionStatus = MutableLiveData<String>().apply {
        value = "서비스 상태"
    }
    var text_ServiceConnectionStatus: LiveData<String> = _text_ServiceConnectionStatus

    private var _image_login = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_login: LiveData<Int> = _image_login

    // =============================================================================================

    // SheetTitle
    private var _text_sheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_sheetTitle: LiveData<String> = _text_sheetTitle

    private var _image_loadedsheetInfo = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_loadedsheetInfo: LiveData<Int> = _image_loadedsheetInfo

    // =============================================================================================

    private var _text_worksheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_worksheetTitle: LiveData<String> = _text_worksheetTitle


    private var _image_loadedworksheetInfo = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_loadedworksheetInfo: LiveData<Int> = _image_loadedworksheetInfo

    // =============================================================================================

    private var _text_preorder_info_sheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_preorder_info_sheetTitle: LiveData<String> = _text_preorder_info_sheetTitle

    private var _text_mail_order_sheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_mail_order_sheetTitle: LiveData<String> = _text_mail_order_sheetTitle

    private var _text_grasping_demand_sheetTitle = MutableLiveData<String>().apply {
        value = ""
    }
    var text_grasping_demand_sheetTitle: LiveData<String> = _text_grasping_demand_sheetTitle


    private var _image_preorder_info_sheet = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_preorder_info_sheet: LiveData<Int> = _image_preorder_info_sheet

    private var _image_mail_order_sheet = MutableLiveData<Int>().apply() {
        value = 0
    }
    var image_mail_order_sheet: LiveData<Int> = _image_mail_order_sheet

    private var _image_grasping_demand_sheet = MutableLiveData<Int>().apply {
        value = 0
    }
    var image_grasping_demand_sheet: LiveData<Int> = _image_grasping_demand_sheet

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

    fun getSheet(view : View, sheetId: String, preorder_SheetNumber: Int, mail_order_SheetNumber: Int, grasping_Demand_SheetNumber: Int) {
        viewModelScope.launch {
            val result2 = try {
                PythonClass.getSheet(sheetId)
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

            getWorksheetTitle(view, sheetId, preorder_SheetNumber, mail_order_SheetNumber, grasping_Demand_SheetNumber, isLoadedSheetId)
        }
    }

    fun getWorksheetTitle(view: View, sheetId: String, preorder_SheetNumber: Int, mail_order_SheetNumber: Int, grasping_Demand_SheetNumber: Int, isLoadedSheetInfo: Boolean) {
        viewModelScope.launch {
            if (isLoadedSheetInfo == false) {
                _text_worksheetTitle.value = "없음"
                _text_preorder_info_sheetTitle.value = "없음"
                _text_mail_order_sheetTitle.value = "없음"
                _text_grasping_demand_sheetTitle.value = "없음"

                _image_loadedworksheetInfo.value = R.drawable.cancel_24dp
                _image_preorder_info_sheet.value = R.drawable.cancel_24dp
                _image_mail_order_sheet.value = R.drawable.cancel_24dp
                _image_grasping_demand_sheet.value = R.drawable.cancel_24dp
                Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                _text_worksheetTitle.value = "공사 중..."
                _image_loadedworksheetInfo.value = R.drawable.cancel_24dp

                val result_preorder = try {
                    PythonClass.getWorkSheet(sheetId, preorder_SheetNumber)
                    Log.d("Debug", "preorder_SheetNumber : " + preorder_SheetNumber)
                    Log.d("Debug", "preorder_SheetTitle : " +
                            PythonClass.getVariable("worksheet")?.get("title").toString())
                    PythonClass.getVariable("worksheet")?.get("title").toString()
                } catch (e: PyException) {
                    "null"
                }

                val result_mail_order = try {
                    PythonClass.getWorkSheet(sheetId, mail_order_SheetNumber)
                    Log.d("Debug", "mail_order_SheetNumber : " + mail_order_SheetNumber)
                    Log.d("Debug", "mail_order_SheetTitle : " +
                            PythonClass.getVariable("worksheet")?.get("title").toString())
                    PythonClass.getVariable("worksheet")?.get("title").toString()
                } catch (e: PyException) {
                    "null"
                }

                val result_grasping_demand = try {
                    PythonClass.getWorkSheet(sheetId, grasping_Demand_SheetNumber)
                    Log.d("Debug", "grasping_Demand_SheetNumber : " + grasping_Demand_SheetNumber)
                    Log.d("Debug", "grasping_Demand_SheetTitle : " +
                            PythonClass.getVariable("worksheet")?.get("title").toString())
                    PythonClass.getVariable("worksheet")?.get("title").toString()
                } catch (e: PyException) {
                    "null"
                }

                when {
                    result_preorder != "null" -> {
                        _text_preorder_info_sheetTitle.value = "선입금 시트 : " + result_preorder
                        _image_preorder_info_sheet.value = R.drawable.check_circle_24dp
                    }

                    else -> {
                        _text_preorder_info_sheetTitle.value = "없음"
                        _image_preorder_info_sheet.value = R.drawable.cancel_24dp
                        Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                when {
                    result_mail_order != "null" -> {
                        _text_mail_order_sheetTitle.value = "통판 시트 : " + result_mail_order
                        _image_mail_order_sheet.value = R.drawable.check_circle_24dp
                    }

                    else -> {
                        _text_mail_order_sheetTitle.value = "없음"
                        _image_mail_order_sheet.value = R.drawable.cancel_24dp
                        Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                when {
                    result_grasping_demand != "null" -> {
                        _text_grasping_demand_sheetTitle.value = "수요조사 시트 : " + result_grasping_demand
                        _image_grasping_demand_sheet.value = R.drawable.check_circle_24dp
                    }

                    else -> {
                        _text_grasping_demand_sheetTitle.value = "없음"
                        _image_grasping_demand_sheet.value = R.drawable.cancel_24dp
                        Snackbar.make(view, "워크 시트 정보를 불러올 수 없습니다", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}