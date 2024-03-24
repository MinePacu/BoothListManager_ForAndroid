package com.minepacu.boothlistmanager.ui.home

import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.minepacu.boothlistmanager.databinding.FragmentHomeBinding
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var prefs: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val textView_ServiceConnectionStatus: TextView = binding.textServiceConnectionStatus
        homeViewModel.text_ServiceConnectionStatus.observe(viewLifecycleOwner) {
            textView_ServiceConnectionStatus.text = it
        }

        val textView_SheetTitle: TextView = binding.sheetTitle
        homeViewModel.text_sheetTitle.observe(viewLifecycleOwner) {
            textView_SheetTitle.text = it
        }

        val textView_WorkSheetTitle: TextView = binding.worksheetTitle
        homeViewModel.text_worksheetTitle.observe(viewLifecycleOwner) {
            textView_WorkSheetTitle.text = it
        }

        val image_Login: ImageView = binding.imageLogin
        homeViewModel.image_login.observe(viewLifecycleOwner) {
            image_Login.setImageResource(it)
        }

        val image_loadedsheetInfo: ImageView = binding.imageLoadedsheetInfo
        homeViewModel.image_loadedsheetInfo.observe(viewLifecycleOwner) {
            image_loadedsheetInfo.setImageResource(it)
        }

        val image_loadedworkSheetInfo: ImageView = binding.imageLoadedworksheetInfo
        homeViewModel.image_loadedworksheetInfo.observe(viewLifecycleOwner) {
            image_loadedworkSheetInfo.setImageResource(it)
        }

        if (!Python.isStarted()) {
            getContext()?.let { AndroidPlatform(it) }?.let { Python.start(it) }
        }

        if (homeViewModel.isLoginToGoogleAPI == false) {
            binding.buttonReloadsheetInfo.isEnabled = false
            homeViewModel.loginToGoogleAPI(root, binding.buttonReloadsheetInfo)
        }
        if (homeViewModel.isLoadedSheetId == false) {
            prefs.getString("sheetId", "")?.let {
                homeViewModel.getSheet(root,
                    it, prefs.getString("sheetNumber", "")!!.toInt())
            }
        }

        binding.buttonReloadsheetInfo.setOnClickListener {
            textView_SheetTitle.text = "로드 중....."
            homeViewModel.isLoadedSheetId = false
            prefs.getString("sheetId", "")?.let {
                homeViewModel.getSheet(root,
                    it, prefs.getString("sheetNumber", "")!!.toInt())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}