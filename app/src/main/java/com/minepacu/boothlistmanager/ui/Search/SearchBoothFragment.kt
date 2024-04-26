package com.minepacu.boothlistmanager.ui.Search

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.PyException
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.boothList.BoothsAdapter
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.databinding.FragmentSearchboothBinding
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage

const val BOOTHNAME = "boothName"

class SearchBoothFragment : Fragment() {

    private var _binding: FragmentSearchboothBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val searchBoothViewModel by viewModels<SearchBoothViewModel> {
        BoothInfoListViewModelFactory(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchboothBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val customProgressPage = this.context?.let { ProgressPage(it) }

        customProgressPage?.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        val boothsAdapter = BoothsAdapter { boothInfo -> adapterOnClick(boothInfo) }

        val recyclerView: RecyclerView = binding.recyclerViewSearchResult
        recyclerView.adapter = boothsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        searchBoothViewModel.boothsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                boothsAdapter.submitList(it as MutableList<BoothInfo>)
            }
        }

        binding.selectionSheetSearch.addOnButtonCheckedListener { buttonToggleGroup, checkedId, isChecked ->
            changeSheetNumber(view, prefs, buttonToggleGroup)
        }

        binding.searchBoothButton.setOnClickListener {
            customProgressPage?.show()
            when {
                binding.selectionSearchType.checkedButtonId == R.id.boothname_search && binding.editTextSearchword.text.toString() != "" ->
                    searchBoothViewModel.searchBoothInfo(it, customProgressPage, boothname = binding.editTextSearchword.text.toString())
                binding.selectionSearchType.checkedButtonId == R.id.boothnumber_search && binding.editTextSearchword.text.toString() != "" ->
                    searchBoothViewModel.searchBoothInfo(it, customProgressPage, boothnumber = binding.editTextSearchword.text.toString())
                binding.selectionSearchType.checkedButtonId == R.id.genre_search && binding.editTextSearchword.text.toString() != "" ->
                    searchBoothViewModel.searchBoothInfo(it, customProgressPage, boothgenre = binding.editTextSearchword.text.toString())
            }
        }
        return root
    }

    /**
     * UI [MaterialButtonToggleGroup]에서 선택된 버튼에 따라
     *
     * 부스 정보를 등록할 워크 시트를 파이썬 모듈 [PythonClass.boothListManagementModule]에 설정합니다.
     *
     * 매개 변수 [buttonToggleGroup]이 null이면 워크 시트가 설정되지 않습니다.
     *
     * @param view 오류가 생겼을 때, [Snackbar]를 출력할 [View]
     * @param prefs 앱의 설정 값을 저장 중인 [SharedPreferences] 객체
     * @param buttonToggleGroup 선입금, 통판, 수요조사 시트 중 하나를 선택할 수 있는 [MaterialButtonToggleGroup] UI 요소
     */
    private fun changeSheetNumber(view: View?, prefs: SharedPreferences, buttonToggleGroup: MaterialButtonToggleGroup?) {
        if (buttonToggleGroup == null) {
            return
        }

        if (buttonToggleGroup.checkedButtonId == R.id.preorderButton_search) {
            try {
                val sheetIndex_Set = prefs.getString("sheetNumber", "")
                val updatesheetIndex_Set = prefs.getString("updateSheetNumber", "")

                PythonClass.setVariable("sheetNumber", sheetIndex_Set?.toInt())
                PythonClass.setVariable(
                    "UpdateLogSheetNumber",
                    updatesheetIndex_Set?.toInt()
                )

                Log.d("Debug", "sheetNumber is updated to " + sheetIndex_Set)
            } catch (e: PyException) {
                view?.let {
                    Snackbar.make(
                        it,
                        "Error : " + e.message,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        } else if (buttonToggleGroup.checkedButtonId == R.id.maIlorderButton_search) {
            try {
                val sheetIndex_Set = prefs.getString("mail_order_sheet_Index", "")
                val updatesheetIndex_Set =
                    prefs.getString("update_mail_order_sheetIndex", "")

                PythonClass.setVariable("sheetNumber", sheetIndex_Set?.toInt())
                PythonClass.setVariable(
                    "UpdateLogSheetNumber",
                    updatesheetIndex_Set?.toInt()
                )

                Log.d("Debug", "sheetNumber is updated to " + sheetIndex_Set)
            } catch (e: PyException) {
                view?.let {
                    Snackbar.make(
                        it,
                        "Error : " + e.message,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        } else if (buttonToggleGroup.checkedButtonId == R.id.graspingdemandButton_search) {
            try {
                val sheetIndex_Set = prefs.getString("grasping_demand_sheet_Index", "")
                val updatesheetIndex_Set =
                    prefs.getString("update_grasping_demand_sheetIndex", "")

                PythonClass.setVariable("sheetNumber", sheetIndex_Set?.toInt())
                PythonClass.setVariable(
                    "UpdateLogSheetNumber",
                    updatesheetIndex_Set?.toInt()
                )

                Log.d("Debug", "sheetNumber is updated to " + sheetIndex_Set)
            } catch (e: PyException) {
                view?.let {
                    Snackbar.make(
                        it,
                        "Error : " + e.message,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }
    }

    private fun adapterOnClick(boothInfo: BoothInfo) {
        val intent = Intent(context, SearchBoothFragment::class.java)
        intent.putExtra(BOOTHNAME, boothInfo.boothname)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}