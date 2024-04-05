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
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.databinding.FragmentHomeBinding
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
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
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preference, false)

        // Google API Service Status
        val textView_ServiceConnectionStatus: TextView = binding.textServiceConnectionStatus
        homeViewModel.text_ServiceConnectionStatus.observe(viewLifecycleOwner) {
            textView_ServiceConnectionStatus.text = it
        }

        // Google API Login Status Icon
        val image_Login: ImageView = binding.imageLogin
        homeViewModel.image_login.observe(viewLifecycleOwner) {
            image_Login.setImageResource(it)
        }

        // =======================================================================================

        // Connected Sheet Title
        val textView_SheetTitle: TextView = binding.sheetTitle
        homeViewModel.text_sheetTitle.observe(viewLifecycleOwner) {
            textView_SheetTitle.text = it
        }

        val image_loadedsheetInfo: ImageView = binding.imageLoadedsheetInfo
        homeViewModel.image_loadedsheetInfo.observe(viewLifecycleOwner) {
            image_loadedsheetInfo.setImageResource(it)
        }

        // =======================================================================================

        // WorkSheet_TItle
        val textView_WorkSheetTitle: TextView = binding.worksheetTitle
        homeViewModel.text_worksheetTitle.observe(viewLifecycleOwner) {
            textView_WorkSheetTitle.text = it
        }

        val image_loadedworkSheetInfo: ImageView = binding.imageLoadedworksheetInfo
        homeViewModel.image_loadedworksheetInfo.observe(viewLifecycleOwner) {
            image_loadedworkSheetInfo.setImageResource(it)
        }

        // ======================================================================================

        val textView_preoder_info_sheetTitle: TextView = binding.preorderInfoSheetTitle
        homeViewModel.text_preorder_info_sheetTitle.observe(viewLifecycleOwner) {
            textView_preoder_info_sheetTitle.text = it
        }

        val textView_mail_order_sheetTitle: TextView = binding.mailOrderSheetTitle
        homeViewModel.text_mail_order_sheetTitle.observe(viewLifecycleOwner) {
            textView_mail_order_sheetTitle.text = it
        }

        val textView_grasping_demand_sheetTitle: TextView = binding.graspingDemandSheetTitle
        homeViewModel.text_grasping_demand_sheetTitle.observe(viewLifecycleOwner) {
            textView_grasping_demand_sheetTitle.text = it
        }

        val image_preorder_info_sheet: ImageView = binding.imagePreorderInfoSheet
        homeViewModel.image_preorder_info_sheet.observe(viewLifecycleOwner) {
            image_preorder_info_sheet.setImageResource(it)
        }

        val image_mail_order_sheet: ImageView = binding.imageMailOrderSheet
        homeViewModel.image_mail_order_sheet.observe(viewLifecycleOwner) {
            image_mail_order_sheet.setImageResource(it)
        }

        val image_grasping_demand_sheet: ImageView = binding.imageGraspingDemandSheet
        homeViewModel.image_grasping_demand_sheet.observe(viewLifecycleOwner) {
            image_grasping_demand_sheet.setImageResource(it)
        }

        // ========================================================================================

        if (!Python.isStarted()) {
            getContext()?.let { AndroidPlatform(it) }?.let { Python.start(it) }
        }

        val sheetId = prefs.getString("sheetId", "")
        PythonClass.setVariable("sheetId", sheetId)

        if (homeViewModel.isLoginToGoogleAPI == false) {
            binding.buttonReloadsheetInfo.isEnabled = false
            homeViewModel.loginToGoogleAPI(root, binding.buttonReloadsheetInfo, binding.buttonReloadworksheetInfo)
        }
        if (homeViewModel.isLoadedSheetId == false) {
            binding.buttonReloadworksheetInfo.isEnabled = false
            prefs.getString("sheetId", "")?.let {
                homeViewModel.getSheet(root,
                    it, prefs.getString("sheetNumber", "")!!.toInt(), prefs.getString("mail_order_sheet_Index", "")!!.toInt(), prefs.getString("grasping_demand_sheet_Index", "")!!.toInt())
            }
        }

        binding.buttonReloadsheetInfo.setOnClickListener {
            textView_SheetTitle.text = "로드 중....."
            textView_WorkSheetTitle.text = "로드 중....."
            homeViewModel.isLoadedSheetId = false
            prefs.getString("sheetId", "")?.let {
                homeViewModel.getSheet(root,
                    it, prefs.getString("sheetNumber", "")!!.toInt(), prefs.getString("mail_order_sheet_Index", "")!!.toInt(), prefs.getString("grasping_demand_sheet_Index", "")!!.toInt())
            }
        }

        binding.buttonReloadworksheetInfo.setOnClickListener {
            textView_SheetTitle.text = "로드 중....."
            textView_WorkSheetTitle.text = "로드 중....."
            homeViewModel.isLoadedSheetId = false
            prefs.getString("sheetId", "")?.let {
                homeViewModel.getSheet(
                    root,
                    it, prefs.getString("sheetNumber", "")!!.toInt(), prefs.getString("mail_order_sheet_Index", "")!!.toInt(), prefs.getString("grasping_demand_sheet_Index", "")!!.toInt())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}