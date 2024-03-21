package com.minepacu.boothlistmanager.ui.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.databinding.FragmentPreferenceBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentPreferenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentPreferenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parentFragmentManager.beginTransaction()
            .replace(R.id.preferences_layout, SettingsPreferenceFragment())
            .commit()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}