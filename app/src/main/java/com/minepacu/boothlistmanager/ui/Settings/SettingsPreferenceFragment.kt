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
    var mail_order_sheetNumber: Preference? = null
    var grasping_demand_sheetIndexPreference: Preference? = null

    var updateSheetNumberPreference: Preference? = null
    var update_mail_order_sheetIndex: Preference? = null
    var update_grasping_demand_sheetIndex: Preference? = null

    var sheetStartIndexPreference: Preference? = null
    var sheetRowHeightPerLinePreference: Preference? = null

    var updateSheetNamePreference: Preference? = null
    var updateSheetStartIndexPreference: Preference? = null
    var updateLogType: Preference? = null

    var update_mail_order_sheetStartIndex: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        if (rootKey == null) {
            sheetIdPreference = findPreference("sheetId")

            sheetNumberPreference = findPreference("sheetNumber")
            mail_order_sheetNumber = findPreference("mail_order_sheet_Index")
            grasping_demand_sheetIndexPreference = findPreference("grasping_demand_sheet_Index")

            updateSheetNumberPreference = findPreference("updateSheetNumber")
            update_mail_order_sheetIndex = findPreference("update_mail_order_sheetIndex")
            update_grasping_demand_sheetIndex = findPreference("update_grasping_demand_sheetIndex")

            sheetStartIndexPreference = findPreference("sheetStartIndex")
            sheetRowHeightPerLinePreference = findPreference("sheetRowHeightPerLine")

            updateSheetNamePreference = findPreference("updateSheetName")
            updateSheetStartIndexPreference = findPreference("updateSheetStartIndex")
            updateLogType = findPreference("updateLogType")

            update_mail_order_sheetStartIndex = findPreference("update_mail_order_SheetStartIndex")
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {

                // Category : General
                "sheetId" -> {
                    val sheetId_Set = prefs.getString("sheetId", "")
                    try {
                        PythonClass.setVariable("sheetId", sheetId_Set)
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                // Category : List_sheet_Index_Category
                "sheetNumber" -> {
                    val sheetNumber_Set = prefs.getString("sheetNumber", "")
                    try {
                        PythonClass.setVariable("sheetNumber", sheetNumber_Set?.toInt())
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

                "grapsing_demand_sheet_Index" -> {

                }

                // Category : update_log_sheet_Index_category
                "updateSheetNumber" -> {
                    val updateSheetNumber_Set = prefs.getString("updateSheetNumber", "")
                    try {
                        PythonClass.setVariable("UpdateSheetNumber", updateSheetNumber_Set?.toInt())
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

                "update_grasping_demand_sheetIndex" -> {

                }

                // Category : list_sheet_category
                "sheetStartIndex" -> {
                    val sheetStartIndex_Set = prefs.getString("sheetStartIndex", "")
                    try {
                        PythonClass.setVariable("sheetStartIndex", sheetStartIndex_Set?.toInt())
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                "sheetRowHeightPerLine" -> {
                    val sheetRowHeightPerLine_Set = prefs.getString("sheetRowHeightPerLine", "")
                    try {
                        PythonClass.setVariable("sheetRowHeightPerLine", sheetRowHeightPerLine_Set?.toInt())
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                // Category : update_category
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

                "updateLogType" -> {
                    val updateLogType_Set = prefs.getString("updateLogType", "")
                    try {
                        if (updateLogType_Set == "Custom") {
                            PythonClass.setVariable("updateLogType", 4)
                        } else {
                            PythonClass.setVariable("updateLogType", 1)
                        }
                    } catch (e: PyException) {
                        Result.Error(Exception(e.message))
                    }
                }

                // Category : update_mail_order_category
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