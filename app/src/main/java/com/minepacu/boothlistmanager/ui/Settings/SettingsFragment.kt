package com.minepacu.boothlistmanager.ui.Settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.minepacu.boothlistmanager.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

}