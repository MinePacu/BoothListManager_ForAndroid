package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.content.SharedPreferences
import android.graphics.Color
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
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.databinding.FragmentHyperlinkgeneratorBinding
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import java.text.SimpleDateFormat
import java.util.Locale

class HyperLinkGeneratorFragment : Fragment() {

    private var _binding: FragmentHyperlinkgeneratorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val hyperLinkGeneratorViewModel =
            ViewModelProvider(this).get(HyperLinkGeneratorViewModel::class.java)

        _binding = FragmentHyperlinkgeneratorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val customProgressPage = this.context?.let { ProgressPage(it) }

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding.filledCopyToClipBoardButton.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            var copiedString = ""

            if ((binding.editLinkSheetnumber.text.toString() != "") &&
                (binding.editLinkBoothCell.text.toString() != "")) {
                val sheetId = prefs.getString("sheetId", "")
                val sheetNumber = binding.editLinkSheetnumber.text.toString().toInt()

                customProgressPage?.show()
                val job = hyperLinkGeneratorViewModel.getGid(root, requireContext(),
                    sheetId!!, sheetNumber, binding.editLinkBoothCell.text.toString(), binding.editLinklabel.text.toString())
                if (job.isCompleted) {
                    customProgressPage?.hide()
                }
            } else if (binding.editLinklabel.text.toString() != "") {
                copiedString =
                    "=HYPERLINK(\"" + binding.editLink.text.toString() + "\", \"" + binding.editLinklabel.text.toString() + "\")"
                hyperLinkGeneratorViewModel.textCopyThenPost(requireContext(), copiedString)
            } else {
                Snackbar.make(root, "하이퍼링크 함수를 만들 수 없습니다. [시트 넘버, 시트 a1 값] 또는 링크 라벨이 빈 칸이 아니여야 합니다.", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        binding.EmptyTextFieldButton.setOnClickListener {
            binding.editLinkSheetnumber.setText("")
            binding.editLinkBoothCell.setText("")
            binding.editLinklabel.setText("")
            binding.editLink.setText("")
        }

        binding.filledAddUpdateLogButton.setOnClickListener {
            customProgressPage?.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            customProgressPage?.show()

            changeSheetNumber(it, prefs, binding.selectionSheet)

            hyperLinkGeneratorViewModel.addUpdateLog(
                root, customProgressPage,
                binding.editBoothnameUpdate.text.toString(), binding.editLinkUpdate.text.toString(), binding.editOffsetUpdate.text.toString().toInt())
        }


        binding.selectionSheet.addOnButtonCheckedListener { buttonToggleGroup, checkedId, isChecked ->
            changeSheetNumber(view, prefs, buttonToggleGroup)
        }


        binding.addDateButton.setOnClickListener {
            val datepicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("마감 일자 선택").build()

            datepicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("M/d(EEE)", Locale.KOREA)
                val date = sdf.format(it)

                binding.editDate.setText(date)
            }

            datepicker.show(getParentFragmentManager(), "tag")
        }

        binding.addClockButton.setOnClickListener {
            val timepicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12).setMinute(0)
                .setTitleText("마감 시간 선택").build()

            timepicker.addOnPositiveButtonClickListener {
                if (timepicker.minute != 0) {
                    binding.editClock.setText(timepicker.hour.toString() + "시 " + timepicker.minute + "분")
                } else {
                    binding.editClock.setText(timepicker.hour.toString() + "시")
                }
            }

            timepicker.show(parentFragmentManager, "tag")
        }

        binding.CopyToClipBoardButtonDate.setOnClickListener {
            var copystring = ""
            if (binding.editClock.text.toString() != "") {
                copystring = "=TEXTJOIN(CHAR(10), 0, \"" + binding.editDate.text.toString() + "\", \"" + binding.editClock.text.toString() + "\")"
            } else {
                copystring = binding.editDate.text.toString()
            }

            hyperLinkGeneratorViewModel.textCopyThenPost(requireContext(), copystring)
        }

        binding.EmptyTextFieldButtonDate.setOnClickListener {
            binding.editDate.setText("")
            binding.editClock.setText("")
        }

        binding.selectionSheetMovingData.addOnButtonCheckedListener { buttonToggleGroup, checkedId, isChecked ->
            changeSheetNumber(view, prefs, buttonToggleGroup)
        }

        binding.moveBoothDataButton.setOnClickListener {
            val originRowIndex = binding.editOriginrownumber.text.toString().toInt()
            val moveRowIndex = binding.editMovedrownumber.text.toString().toInt()

            customProgressPage?.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            customProgressPage?.show()

            hyperLinkGeneratorViewModel.moveBoothData(it, customProgressPage, originRowIndex, moveRowIndex)
        }

        binding.EmptyTextFieldButtonMoveinfotool.setOnClickListener {
            binding.editOriginrownumber.setText("")
            binding.editMovedrownumber.setText("")
        }

        binding.selectionSheetPutBoothNumber.addOnButtonCheckedListener { buttonToggleGroup, checkedId, isChecked ->
            changeSheetNumber(view, prefs, buttonToggleGroup)
        }

        binding.putBoothNumberButton.setOnClickListener {
            val boothname_put = binding.editBoothnameToputBoothNumber.text.toString()
            val boothNumber_put = binding.editBoothnumberToputBoothNumber.text.toString()

            customProgressPage?.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            customProgressPage?.show()

            hyperLinkGeneratorViewModel.putBoothNumberToSpecificBooth(it, customProgressPage, boothname_put, boothNumber_put)
        }

        binding.EmptyTextFieldButtonPutBoothNumber.setOnClickListener {
            binding.editBoothnameToputBoothNumber.setText("")
            binding.editBoothnumberToputBoothNumber.setText("")
        }

        textWatcher()
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
        binding.editLinklabel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editLinklabel.text!!.isEmpty()) {
                    binding.filledCopyToClipBoardButton.isEnabled = false
                    binding.editLinkLabelLayout.error = "연결할 링크의 라벨를 입력해주세요."
                } else {
                    binding.filledCopyToClipBoardButton.isEnabled = true
                    binding.editLinkLabelLayout.error = null
                }
            }
        })

        binding.editBoothnameUpdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editBoothnameUpdate.text!!.isEmpty()) {
                    binding.filledAddUpdateLogButton.isEnabled = false
                    binding.editBoothnameUpdate.error = "업데이트 로그에 등록할 부스 이름을 입력하세요."
                } else {
                    binding.filledAddUpdateLogButton.isEnabled = true
                    binding.editBoothnameUpdate.error = null
                }
            }
        })

        binding.editLinkUpdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editLinkUpdate.text!!.isEmpty()) {
                    binding.filledAddUpdateLogButton.isEnabled = false
                    binding.editLinkUpdate.error = "업데이트 로그에 등록할 링크 이름을 입력하세요."
                } else {
                    binding.filledAddUpdateLogButton.isEnabled = true
                    binding.editLinkUpdate.error = null
                }
            }
        })

        binding.editOffsetUpdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.editOffsetUpdate.text!!.isEmpty()) {
                    binding.filledAddUpdateLogButton.isEnabled = false
                    binding.editOffsetUpdate.error = "하이퍼링크에 적용할 오프셋 값을 입력하세요."
                } else {
                    binding.filledAddUpdateLogButton.isEnabled = true
                    binding.editOffsetUpdate.error = null
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}