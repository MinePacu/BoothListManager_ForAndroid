package com.minepacu.boothlistmanager.tools.PythonCode

import android.util.Log
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PythonClass {

    companion object {

        private val py = Python.getInstance()
        private val boothListManagementModule =
            py.getModule("BoothListManagementModule")

        /**
         * 구글 API에 로그인된 정보를 저장하는 객체
         */
        private var loginInfo: PyObject? = null

        /**
         * 시트의 정보를 담은 객체
         */
        var sheetInfo: PyObject? = null

        var now_WorkSheet: PyObject? = null

        /**
         * 구글 API에 로그인하여 반환된 객체를 LoginInfo에 저장합니다.
         * 이 LoginInfo 객체는 파이썬 내부의 boothListManagementModule 객체 안의 gc라는 변수에도
         * 자동으로 저장됩니다.
         * @return LoginInfo 객체의 유효 여부
         * @see loginInfo
         */
        suspend fun loginToGoogleAPI(): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun LogintoGoogleAPI is Executed")
                loginInfo = boothListManagementModule.callAttr("loginService")
                Log.d(
                    "Debug",
                    when {
                        (loginInfo == null) -> "IsNull of loginInfo : true"
                        else -> "IsNull of loginInfo : false"
                    }
                )
                if (loginInfo != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("로그인 불가"))
                }
            }
        }

        /**
         * 시트 정보를 불러와서 sheetInfo 객체에 저장합니다.
         * @return sheetInfo 객체의 유효 여부
         */
        suspend fun getSheetInfo(sheetId: String): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun getsheetInfo is Executed")
                sheetInfo =
                    boothListManagementModule.callAttr(
                        "getSheet", sheetId
                    )
                Log.d(
                    "Debug",
                    when {
                        (sheetInfo == null) -> "IsNull of sheetInfo : true"
                        else -> "IsNull of sheetInfo : false"
                    }
                )
                if (sheetInfo != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("시트 정보를 불러올 수 없음"))
                }
            }
        }

        /**
         * 부스 정보를 시트에 추가합니다.
         * @param boothInfo: 추가할 부스의 정보
         * @return 함수 addBoothInfoToSheet의 정상 수행 여부
         * @throws PyException: 파이썬 모듈 내에 정의된 addBoothInfoToSheet 함수에서 throw한 예외
         */
        suspend fun addBoothInfoToSheet(boothInfo: BoothInfo): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun addBoothInfoToSheet is Executed")
                var result: PyObject? = null
                result = boothListManagementModule.callAttr(
                    "addBoothInfoToSheet",
                    boothInfo.boothnumber,
                    boothInfo.boothname,
                    boothInfo.genres,
                    boothInfo.yoil,
                    boothInfo.InfoLabel,
                    boothInfo.InfoLink,
                    boothInfo.preorder_Date,
                    boothInfo.preorder_Label,
                    boothInfo.preorder_Link
                )

                Log.d("Debug", "boothname : " + boothInfo.boothname)
                Log.d(
                    "Debug",
                    when {
                        (result == null) -> "IsNull of result from addBoothInfoToSheet : true"
                        else -> "IsNull of result from addBoothInfoToSheet : false"
                    }
                )

                if (result != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("시트에 부스 정보를 추가하지 못했습니다."))
                }
            }
        }

        suspend fun addUpdateLog(boothname: String, linkname: String, offset: Int): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun addUpdateLog is Executed")
                var result: PyObject? = null

                result = boothListManagementModule.callAttr(
                    "add_UpdateLog",
                    boothname,
                    linkname,
                    offset)

                Log.d(
                    "Debug",
                    when {
                        (result == null) -> "IsNull of result from addUpdateLog : true"
                        else -> "IsNull of result from addUpdateLog : false"
                    }
                )
                if (result != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("시트에 부스 정보를 추가하지 못했습니다."))
                }
            }
        }

        /**
         * 지정한 매개 변수의 값에 해당하는 워크시트를 가져옵니다.
         * @param sheetId 워크시트가 있는 시트의 ID
         * @param sheetNumber 워크시트가 해당하는 인덱스 값, 0부터 시작합니다.
         * @return 함수의 정상 수행 여부
         */
        suspend fun getSheet_WorkSheet(sheetId: String, sheetNumber: Int): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun getGid is Executed")
                now_WorkSheet = boothListManagementModule.callAttr("getWorkSheet", sheetId, sheetNumber)

                Log.d(
                    "Debug",
                    when {
                        (now_WorkSheet == null) -> "IsNull of result from getGid : true"
                        else -> "IsNull of result from getGid : false"
                    })

                if (now_WorkSheet != null) {
                    //Log.d("Debug", "Result : " + now_worksheet?.get("id").toString())
                    Result.Success(true)
                } else {
                    Result.Error(Exception("시트의 GID 값을 불러오지 못했습니다."))
                }
            }
        }

        fun setVariable(variable_name: String, value: Any?) {
            boothListManagementModule.put(variable_name, value)
        }

        fun getVariable(variable_name: String): PyObject? {
            return boothListManagementModule.get(variable_name)
        }
    }
}
