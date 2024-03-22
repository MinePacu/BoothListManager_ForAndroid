package com.minepacu.boothlistmanager.ui.Settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.chaquo.python.PyException
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.Result
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass

class SettingsPreferenceFragment : PreferenceFragmentCompat() {
    lateinit var prefs: SharedPreferences

    var sheetIdPreference: Preference? = null
    var sheetNumberPreference: Preference? = null
    var updateSheetNamePreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        if (rootKey == null) {
            sheetIdPreference = findPreference("sheetId")
            sheetNumberPreference = findPreference("sheetNumber")
            updateSheetNamePreference = findPreference("updateSheetName")
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preference, false)

        if (prefs.getString("sheetId", "") == "") {
            sheetIdPreference?.summary = "현재 값이 설정되어 있지 않습니다."
        } else {
            sheetIdPreference?.summary = "현재 값 : " + prefs.getString("sheetId", "")
        }

        if (prefs.getString("sheetName", "") == "") {
            sheetNumberPreference?.summary = "현재 값이 설정되어 있지 않습니다."
        } else {
            sheetNumberPreference?.summary = "현재 값 : " + prefs.getString("sheetNumber", "")
        }

        if (prefs.getString("updateSheetName", "") == "") {
            updateSheetNamePreference?.summary = "현재 값이 설정되어 있지 않습니다."
        } else {
            updateSheetNamePreference?.summary =
                "현재 값 : " + prefs.getString("updateSheetName", "")
        }
    }

    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                "sheetId" -> {
                    val sheetId_Set = prefs.getString("sheetId", "")
                    try {
                        PythonClass.setVariable("spreadsheetId", sheetId_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "sheetNumber" -> {
                    val sheetNumber_Set = prefs.getString("sheetNumber", "")
                    try {
                        PythonClass.setVariable("sheetNumber", sheetNumber_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "updateSheetName" -> {
                    val updateSheetName_Set = prefs.getString("updateSheetName", "")
                    try {
                        PythonClass.setVariable("UpdateLogSheetName", updateSheetName_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }
            }
        }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
    }

}