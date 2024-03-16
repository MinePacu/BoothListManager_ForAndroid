package com.minepacu.boothlistmanager.tools.PythonCode

import android.util.Log
import com.chaquo.python.Kwarg
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.minepacu.boothlistmanager.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PythonClass {

    private val py = Python.getInstance()
    private val boothListManagementModule = py.getModule("BoothListManagementModule")

    /**
     * 구글 API에 로그인된 정보를 저장하는 객체
     */
    private var loginInfo : PyObject? = null
    public var sheetInfo : PyObject? = null

    /**
     * 구글 API에 로그인하여 반환된 객체를 LoginInfo에 저장합니다.
     * 이 LoginInfo 객체는 파이썬 내부의 boothListManagementModule 객체 안의 gc라는 변수에도
     * 자동으로 저장됩니다.
     * @return LoginInfo 객체의 유효 여부
     * @see loginInfo
     */
    suspend fun loginToGoogleAPI() : Result<Boolean> {
        return withContext(Dispatchers.IO) {
            Log.d("Debug", "Fun LogintoGoogleAPI_Excuted")
            loginInfo = boothListManagementModule.callAttr("loginService")
            Log.d("Debug",
                when {(loginInfo == null) -> "IsNull of loginInfo : true"
                    else -> "IsNull of loginInfo : false"})
            if (loginInfo != null) {
                Result.Success(true)
            }
            else {
                Result.Error(Exception("로그인 불가"))
            }
        }
    }

    suspend fun getSheetInfo() : Result<Boolean> {
        return withContext(Dispatchers.IO) {
            Log.d("Debug", "Fun getsheetInfo_Excuted")
            sheetInfo =
                boothListManagementModule.callAttr("getSheet",
                    "1TmZxEkJW17d0I1MmfNyzIIxjh1n_en1DKrwsbk2OzjM")
            Log.d("Debug",
                when {(sheetInfo == null) -> "IsNull of sheetInfo : true"
                else -> "IsNull of sheetInfo : false"})
            if (sheetInfo != null) {
                Result.Success(true)
            }
            else {
                Result.Error(Exception("시트 정보를 불러올 수 없음"))
            }
        }
    }
}
