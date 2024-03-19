package com.minepacu.boothlistmanager.ui.Settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.minepacu.boothlistmanager.R

class SettingsPreferenceFragment : PreferenceFragmentCompat() {
    lateinit var prefs: SharedPreferences

    var sheetIdPreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        if (rootKey == null) {
            sheetIdPreference = findPreference("sheetId")
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
    }

    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                "sheetId" -> {

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