package com.minepacu.boothlistmanager.tools.PythonCode

import android.util.Log
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 구글의 스프레드시트와 연동할 수 있는 기능들을 담은 클래스
 *
 * @property py 파이썬 코드를 실행할 인스턴스
 * @property boothListManagementModule 파이썬으로 코딩된 부스 관리 모듈
 */
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
         * 구글 API에 로그인하여 반환된 객체를 [loginInfo]에 저장합니다.
         *
         * 이 LoginInfo 객체는 파이썬 내부의 [boothListManagementModule] 객체 안의 gc라는 변수에도
         * 자동으로 저장됩니다.
         * @return [loginInfo] 객체의 유효 여부
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
         * @return 함수 [addBoothInfoToSheet]의 정상 수행 여부
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
                    boothInfo.authorsNickNames,
                    boothInfo.authorsLinks,
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

        /**
         * 부스 이름, 링크 이름, 오프셋 값을 이용해 업데이트 로그를 수동으로 등록합니다.
         * @param boothname 업데이트 로그에 들어갈 부스 이름
         * @param linkname 업데이트 로그에 들어갈 링크 이름
         * @param offset 해당 부스가 이미 가지고 있는 링크의 수로 해당 링크 수만큼 아래 행으로 내려가 링크를 지정합니다.
         * @param mode 해당 업데이트 로그 시트가 인포 Column에 링크해야 하는 경우, 1로 지정합니다. 그 이외에는 0 을 사용하세요.
         */
        suspend fun addUpdateLog(boothname: String, linkname: String, offset: Int, mode: Int): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun addUpdateLog is Executed")
                var result: PyObject? = null

                result = boothListManagementModule.callAttr(
                    "add_UpdateLog",
                    boothname,
                    linkname,
                    offset,
                    mode)

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
         * 지정한 시트 ID에 해당하는 시트 데이터를 가져옵니다.
         * @param sheetId 가져오려는 시트의 ID
         * @return 함수의 정상 수행 여부
         */
        suspend fun getSheet(sheetId: String): Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun getGid is Executed")
                now_WorkSheet = boothListManagementModule.callAttr("getSheet", sheetId)

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
                    Result.Error(Exception("시트 데이터를 불러오지 못했습니다."))
                }
            }
        }

        /**
         * 지정한 매개 변수의 값에 해당하는 워크시트를 가져옵니다.
         * @param sheetId 워크시트가 있는 시트의 ID
         * @param sheetNumber 워크시트가 해당하는 인덱스 값, 0부터 시작합니다.
         * @return 함수의 정상 수행 여부
         */
        suspend fun getWorkSheet(sheetId: String, sheetNumber: Int): Result<Boolean> {
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
                    Result.Error(Exception("워크 시트 데이터를 불러오지 못했습니다."))
                }
            }
        }

        /**
         * 지정한 originIndex 위치에서 moveIndex 위치로 부스 데이터를 이동시킵니다.
         *
         * *매개 변수의 모든 인덱스는 이동학기 전의 인덱스 값을 기준으로 합니다.*
         *
         * @param originIndex 이동하려는 부스 데이터가 있는 인덱스
         * @param moveIndex 이동하려는 위치의 인덱스
         * @return 함수의 정상 실행 여부를 가진 [Result] 객체
         */
        suspend fun moveBoothData(originIndex: Int, moveIndex: Int) : Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun moveBoothData is Executed")
                val result = boothListManagementModule.callAttr("moveBoothData", originIndex, moveIndex)

                Log.d(
                    "Debug",
                    when {
                        (result == null) -> "IsNull of result from getGid : true"
                        else -> "IsNull of result from getGid : false"
                    })

                if (result != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("부스 정보를 옮기지 못했습니다."))
                }
            }
        }

        /**
         * 매개 변수 [boothname]에 해당하는 부스 데이터에 지정한 부스 번호 ([boothnumber])를 할당합니다.
         *
         * @param boothName 부스 번호를 할당할 부스의 이름
         * @param boothNumber 할당할 부스 번호, 일반적으로 [부스 코드] + [숫자] 조합이며, [부스 코드]에 소문자 알파벳이 포홈된 경우, 자동으로 대문자로 변환합니다.
         * @return 파이썬 모듈 [boothListManagementModule] 에서 해당 부스를 찾으면 해당 셀을 업데이트한 결과를 저장한 JSONResponse 객체를 반환하여 [Result.Success]를 반환하며, 찾지 못하면 None을 반환하여 [Result.Error]를 반환합니다.
         */
        suspend fun putBoothNumbertoSpecificBooth(boothName: String, boothNumber: String) : Result<Boolean> {
            return withContext(Dispatchers.IO) {
                Log.d("Debug", "Fun putBoothNumbertoSpecificBooth is Executed")
                val result = boothListManagementModule.callAttr("putBoothNumbertoSpecificBooth", boothName, boothNumber)

                Log.d(
                    "Debug",
                    when {
                        (result == null) -> "IsNull of result from putBoothNumbertoSpecificBooth : true"
                        else -> "IsNull of result from putBoothNumbertoSpecificBooth : false"
                    })

                if (result != null) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("부스 번호를 등록하지 못했습니다."))
                }
            }
        }

        /**
         * 매개 변수 [label]이 `//` 문자를 가진 경우, *TextJoin* 함수를 이용하여 줄 바꿈 합니다.
         *
         * `//` 문자가 없는 경ㅇ, 매개 변수 [label]를 그대로 반환합니다.
         *
         * @param label 줄 바꿈할 라벨
         * @return 줄 바쑴 여부를 점검 및 적용한 라벨 문자열 또는 에러 문자열
         */
        suspend fun getLabelWithTextJoin(label: String) : String {
            return withContext(Dispatchers.IO) {
                try {
                    Log.d("Debug", "Fun getLabelWithTextJoin is Executed")
                    val result = boothListManagementModule.callAttr("AddTextJoin", label, false)

                    Log.d(
                        "Debug",
                        when {
                            (result == null) -> "IsNull of result from AddTextJoin : true"
                            else -> "IsNull of result from AddTextJoin : false"
                        }
                    )

                    if (result != null) {
                        result.toString()
                    } else {
                        "result is null"
                    }
                }

                catch (e: PyException) {
                    "Error : " + e.message.toString()
                }
            }
        }

        suspend fun searchBoothInfo(boothNumber: String? = null, boothName: String? = null, boothGenre: String? = null) : BoothInfo? {
            return withContext(Dispatchers.IO) {
                try {
                    Log.d("Debug", "Fun searchBoothInfo is Executed")
                    val result = boothListManagementModule.callAttr("SearchBooth", boothNumber, boothName, boothGenre)

                    Log.d(
                        "Debug",
                        when {
                            (result == null) -> "IsNull of result from SearchBooth : true"
                            else -> "IsNull of result from SearchBooth : false"
                        }
                    )

                    when {
                        result != null -> {
                            val boothnumber: String = result.asList().get(0).toString()
                            val boothname: String = result.asList().get(1).toString()
                            val boothgenre: String = result.asList().get(2).toString()
                            val authorsNickNames: List<String> = listOf("")
                            val authorsLinks: List<String> = listOf("")
                            val yoil: String = result.asList().get(3).toString()
                            val infoLabel: String = result.asList().get(4).toString()
                            val infoLink: String = result.asList().get(4).toString()
                            val pre_Order_Date: String = result.asList().get(5).toString()
                            val pre_Order_Label: String = result.asList().get(6).toString()
                            val pre_Order_Link: String = result.asList().get(7).toString()

                            BoothInfo(boothnumber, boothname, boothgenre, authorsNickNames, authorsLinks, yoil, infoLabel, infoLink, pre_Order_Date, pre_Order_Label, pre_Order_Link)
                        }
                        else -> null
                    }
                }

                catch (e: PyException) {
                    Log.d("Debug", "Error : ${e.message}")
                    null
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
