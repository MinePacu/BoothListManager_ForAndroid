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
    var sheetStartIndex: Preference? = null

    var updateSheetNumberPreference: Preference? = null
    var updateSheetNamePreference: Preference? = null
    var updateSheetStartIndexPreference: Preference? = null

    var mail_order_sheetNumber: Preference? = null

    var update_mail_order_sheetIndex: Preference? = null
    var update_mail_order_sheetStartIndex: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        if (rootKey == null) {
            sheetIdPreference = findPreference("sheetId")

            sheetNumberPreference = findPreference("sheetNumber")
            sheetStartIndex = findPreference("sheetStartIndex")

            updateSheetNumberPreference = findPreference("updateSheetNumber")
            updateSheetNamePreference = findPreference("updateSheetName")
            updateSheetStartIndexPreference = findPreference("updateSheetStartIndex")

            mail_order_sheetNumber = findPreference("mail_order_sheet_Index")

            update_mail_order_sheetIndex = findPreference("update_mail_order_sheetIndex")
            update_mail_order_sheetStartIndex = findPreference("update_mail_order_SheetStartIndex")
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preference, false)
    }

    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                "sheetId" -> {
                    val sheetId_Set = prefs.getString("sheetId", "")
                    try {
                        PythonClass.setVariable("sheetId", sheetId_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "sheetNumber" -> {
                    val sheetNumber_Set = prefs.getString("sheetNumber", "")
                    try {
                        PythonClass.setVariable("sheetNumber", sheetNumber_Set?.toInt())
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "sheetStartIndex" -> {
                    val sheetStartIndex_Set = prefs.getString("sheetStartIndex", "")
                    try {
                        PythonClass.setVariable("sheetStartIndex", sheetStartIndex_Set?.toInt())
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "updateSheetNumber" -> {
                    val updateSheetNumber_Set = prefs.getString("updateSheetNumber", "")
                    try {
                        PythonClass.setVariable("UpdateSheetNumber", updateSheetNumber_Set?.toInt())
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

                "updateSheetStartIndex" -> {
                    val updateSheetName_Set = prefs.getString("updateSheetStartIndex", "")
                    try {
                        PythonClass.setVariable("updateSheetStartIndex", updateSheetName_Set?.toInt())
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "mail_order_sheet_Index" -> {
                    val mail_order_sheet_Index_Set = prefs.getString("mail_order_sheet_Index", "")
                    try {
                        PythonClass.setVariable("mail_order_sheetIndex", mail_order_sheet_Index_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "update_mail_order_sheetIndex" -> {
                    val update_mail_order_sheetIndex_Set = prefs.getString("update_mail_order_sheetIndex", "")
                    try {
                        PythonClass.setVariable("update_mail_order_sheetIndex", update_mail_order_sheetIndex_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "update_mail_order_SheetStartIndex" -> {
                    val update_mail_order_sheetStartIndex_Set = prefs.getString("update_mail_order_sheetStartIndex", "")
                    try {
                        PythonClass.setVariable("update_mail_order_sheetStartIndex", update_mail_order_sheetStartIndex_Set)
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