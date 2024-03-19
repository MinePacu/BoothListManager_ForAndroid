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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        if (rootKey == null) {
            sheetIdPreference = findPreference("sheetId")
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        if (prefs.getString("sheetId", "") == "") {
            sheetIdPreference?.summary = "현재 값이 설정되어 있지 않습니다."
        }
    }

    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                "sheetId" -> {
                    val sheetId_Set = prefs.getString("sheetId", "")
                    val reuslt = try {
                        PythonClass.setVariable("sheetId", sheetId_Set)
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