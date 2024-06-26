package com.minepacu.boothlistmanager.ui.Booth

import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.databinding.FragmentAddboothBinding
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Locale

class AddBoothFragment : Fragment() {

    private var _binding: FragmentAddboothBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val addboothViewModel =
                ViewModelProvider(this).get(AddBoothViewModel::class.java)

        _binding = FragmentAddboothBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val addboothButton = binding.filledAddBoothButton
        val emptyTextButton = binding.EmptyTextFieldButton
        val customProgressPage = this.context?.let { ProgressPage(it) }

        customProgressPage?.window?.setBackgroundDrawable(
            ColorDrawable(android.graphics.Color.TRANSPARENT))

        emptyTextButton.setOnClickListener {
            binding.editBoothNumber.editText?.setText("")
            binding.editBoothName.editText?.setText("")
            binding.editGenre.editText?.setText("")
            binding.editInfoLabel.editText?.setText("")
            binding.editInfoLabel.editText?.setText("")
            binding.editPreorderDateLayout.editText?.setText("")
            binding.editPreorderClockLayout.editText?.setText("")
            binding.editPreOrderLabel.editText?.setText("")
            binding.editPreOrderLink.editText?.setText("")
        }

        addboothButton.setOnClickListener{
            val boothnumber = binding.editBoothNumber.editText?.text.toString()
            val boothname = binding.editBoothName.editText?.text.toString()
            val genre = binding.editGenre.editText?.text.toString()
            val yoil = binding.YoilGroup.checkedRadioButtonId
            val infolabel = binding.editInfoLabel.editText?.text.toString()
            val infolink = binding.editInfoLink.editText?.text.toString()
            val preorder_label = binding.editPreOrderLabel.editText?.text.toString()
            val preorder_link = binding.editPreOrderLink.editText?.text.toString()

            var new_yoil = ""
            when {
                yoil == binding.FesInSaturday.id -> new_yoil = "토"
                yoil == binding.FesInSunday.id -> new_yoil = "일"
                yoil == binding.FesInboth.id -> new_yoil = "토/일"
                yoil == binding.FesInUnknown.id -> new_yoil = "?"
            }

            val preorder_date = when {
                binding.editPreOrderClock.text.toString() != "" -> binding.editPreOrderDate.text.toString() + "//" + binding.editPreOrderClock.text.toString()
                else -> binding.editPreOrderDate.text.toString()
            }

            if (genre.contains("//") == true) {
                val tempstr = genre.split("//")
                PythonClass.setVariable("dateline_In_aRow", tempstr.count())
            } else {
                if (binding.editPreOrderClock.text.toString() != "") {
                    PythonClass.setVariable("dateline_In_aRow", 2)
                } else {
                    PythonClass.setVariable("dateline_In_aRow", 1)
                }
            }

            val boothInfo = BoothInfo(boothnumber, boothname, genre, new_yoil,
                infolabel, infolink,
                preorder_date, preorder_label, preorder_link)

            Log.d("Debug", "BoothInfo : " + boothInfo.toString())
            customProgressPage?.show()

            changeSheetNumber(it, prefs, binding.selectionSheet)

            PythonClass.setVariable("sheetId", prefs.getString("sheetId", ""))
            addboothViewModel.addBoothInfoToSheet(root, customProgressPage, boothInfo)
        }

        binding.addPreorderdateButton.setOnClickListener {
            val datepicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("마감 일자 선택")
                .build()

            datepicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("M/d(EEE)", Locale.KOREA)
                val date = sdf.format(it)

                binding.editPreOrderDate.setText(date)
            }

            datepicker.show(parentFragmentManager, "Tag")
        }

        binding.addPreorderclockButton.setOnClickListener {
            val timepicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(12).setMinute(0)
                .setTitleText("마감 시간 선택")
                .build()

            timepicker.addOnPositiveButtonClickListener {
                if (timepicker.minute != 0) {
                    binding.editPreOrderClock.setText(timepicker.hour.toString() + "시 " + timepicker.minute.toString() + "분")
                } else {
                    binding.editPreOrderClock.setText(timepicker.hour.toString() + "시")
                }
            }

            timepicker.show(parentFragmentManager, "Tag")
        }

        binding.selectionSheet.addOnButtonCheckedListener { buttonToggleGroup, checkedId, isChecked ->
            changeSheetNumber(view, prefs, buttonToggleGroup)
        }

        binding.switchDisableAutoLocation.setOnCheckedChangeListener { buttonView, isChekced ->
            PythonClass.setVariable("isIgnoreRecommandLocation", isChekced)
        }

        textWatcher()
        //enterTestBooth()
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

        if (buttonToggleGroup.checkedButtonId == R.id.preorderButton) {
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
        } else if (buttonToggleGroup.checkedButtonId == R.id.maIlorderButton) {
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
        } else if (buttonToggleGroup.checkedButtonId == R.id.graspingdemandButton) {
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

    fun textWatcher() {
        binding.editTextBoothName.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editTextBoothName.text!!.isEmpty()) {
                    binding.editBoothName.error = "부스 이름을 입력해주세요"
                } else {
                    binding.editBoothName.error = null
                }
            }
        })

        binding.editTextGenre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editTextGenre.text!!.isEmpty()) {
                    binding.editGenre.error = "부스가 취급하는 장르를 입력해주세요."
                } else {
                    binding.editGenre.error = null
                }
            }
        })
    }

    fun enterTestBooth() {
        binding.preorderButton.isPressed = false
        binding.editTextBoothNumber.setText("")
        binding.editTextBoothName.setText("")
        binding.editTextGenre.setText(")")
        binding.FesInSaturday.isChecked = false
        binding.editPreOrderDate.setText("")
        binding.editTextPreOrderLabel.setText("")
        binding.editTextPreOrderLink.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}