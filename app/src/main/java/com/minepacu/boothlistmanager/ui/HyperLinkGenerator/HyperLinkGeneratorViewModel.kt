package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.data.model.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HyperLinkGeneratorViewModel : ViewModel() {
    fun getGid(view :View, context: Context, sheetId: String, sheetNumber: Int, boothcell: String, linkLabel: String): Job {
        return viewModelScope.launch {
            val result = PythonClass.getSheet_WorkSheet(sheetId, sheetNumber)

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
}