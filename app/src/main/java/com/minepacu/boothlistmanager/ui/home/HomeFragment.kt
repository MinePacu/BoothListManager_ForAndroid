package com.minepacu.boothlistmanager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.minepacu.boothlistmanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView_ServiceConnectionStatus: TextView = binding.textServiceConnectionStatus
        homeViewModel.text_ServiceConnectionStatus.observe(viewLifecycleOwner) {
            textView_ServiceConnectionStatus.text = it
        }

        val textView_SheetTitle: TextView = binding.sheetTitle
        homeViewModel.text_sheetTitle.observe(viewLifecycleOwner) {
            textView_SheetTitle.text = it
        }

        if (!Python.isStarted()) {
            getContext()?.let { AndroidPlatform(it) }?.let { Python.start(it) }
        }

        homeViewModel.loginToGoogleAPI(root)
        homeViewModel.getSheet(root)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}