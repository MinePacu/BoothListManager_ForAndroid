package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.data.model.Result
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HyperLinkGeneratorViewModel : ViewModel() {
    /**
     * 해당 워크시트의 아이디(Gid) 값을 가져옵니다.
     *
     * Gid 값을 가져오지 못한 경우, [Snackbar]에 오류 내용이 출력됩니다.
     * @param view [SnackBar]를 출력할 [View]
     * @param context [Toast]를 띄울 [context]
     * @param sheetId 가져올 시트의 ID
     * @param sheetNumber 가져올 워크 시트의 인덱스, 이 값은 0부터 시작합니다.
     * @param boothcell 링크로 설정할 a1Notation 값입니다.
     * @param linkLabel 링크의 라벨입니다.
     * @return 백그라운드 작업
     * @see Snackbar
     * @see PythonClass.getSheet_WorkSheet
     */
    fun getGid(view :View, context: Context, sheetId: String, sheetNumber: Int, boothcell: String, linkLabel: String): Job {
        return viewModelScope.launch {
            val result = PythonClass.getWorkSheet(sheetId, sheetNumber)

            when (result) {
                is Result.Success<Boolean> -> {
                    var gid = PythonClass.now_WorkSheet?.get("id")!!.toInt()
                    val copiedString = "=HYPERLINK(\"#gid=" + gid.toString() + "&range=" + boothcell + "\", \"" + linkLabel + "\")"
                    textCopyThenPost(context, copiedString)
                    Snackbar.make(view, "클립보드에 복사되었습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> Snackbar.make(view, "클립보드에 복사되지 않았습니다.", Snackbar.LENGTH_LONG)
                            .show()
            }
        }
    }

    fun textCopyThenPost(context: Context, textCopied: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, textCopied + " 복사됨", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 업데이트 로그 시트에 업데이트 로그를 등록합니다.
     *
     * 일부 매개 변수의 값들로 만들어지는 로그의 함수 식은 다음과 같습니다.
     * ```
     * =HYPERRINK(CONCATENATE("#gid={링크할 워크 시트의 ID}&range={링크할 시트의 a1Notation}",
     *                        SUM(MATCH("{[boothname]", '{링크할 워크 시트의 타이틀}!C:C, 0), offset), "{로그 내용}")
     * ```
     * @param view SnackBar를 출력할 View
     * @param processingRing 로딩 중 화면 오브젝트, 이 함수를 호출하기 전 [android.app.Dialog.show] 함수를 이미 호출한 상태여야 합니다.
     * @param boothname 업데이트 로그에 등록할 부스 이름
     * @param linkName 업데이트 로그에 등록할 링크 이름
     * @param offset boothname에 해당하는 부스가 이미 가지고 있는 링크의 수로 해당 값만큼 아래 행을 링크 대상으로 검색합니다.
     * @see Snackbar
     * @see ProgressPage
     */
    fun addUpdateLog(view: View, processingRing: ProgressPage?, boothname: String, linkName: String, offset: Int): Job {
        return viewModelScope.launch {
            val result = PythonClass.addUpdateLog(boothname, linkName, offset)

            when (result) {
                is Result.Success<Boolean> -> {
                    processingRing?.hide()
                    Snackbar.make(view, "업데이트 로그가 성공적으로 추가되었습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {
                    processingRing?.hide()
                    Snackbar.make(view, "업데이트 로그가 추가되지 못했습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    /**
     * 지정한 originIndex 위치에서 moveIndex 위치로 부스 데이터를 이동시킵니다.
     *
     * *매개 변수의 모든 인덱스는 이동학기 전의 인덱스 값을 기준으로 합니다.*
     *
     * @param view 오류가 생겼을 때, [Snackbar]를 출력할 [View] 객체
     * @param processingRing 이동 중일 때, 출력할 로딩 창 ([ProgressPage]) 객체
     * @param originIndex 이동하려는 부스 데이터가 있는 인덱스
     * @param moveIndex 이동하려는 위치의 인덱스
     * @return 백그라운드 작업 ([Job]) 객체
     */
    fun moveBoothData(view: View, processingRing: ProgressPage?, originIndex: Int, moveIndex: Int) : Job {
        return viewModelScope.launch {
            val result = PythonClass.moveBoothData(originIndex, moveIndex)

            when (result) {
                is Result.Success<Boolean> -> {
                    processingRing?.hide()
                    Snackbar.make(view, "부스 정보를 성공적으로 이동했습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {
                    processingRing?.hide()
                    Snackbar.make(view, "부스 정보를 이동하지 못했습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun putBoothNumberToSpecificBooth(view: View, processingRing: ProgressPage?, boothName: String, boothNumber: String) : Job {
        return viewModelScope.launch {
            val result = PythonClass.putBoothNumbertoSpecificBooth(boothName, boothNumber)

            when (result) {
                is Result.Success<Boolean> -> {
                    processingRing?.hide()
                    Snackbar.make(view, "부스 정보를 성공적으로 이동했습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {
                    processingRing?.hide()
                    Snackbar.make(view, "부스 정보를 이동하지 못했습니다.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}