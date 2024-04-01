package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.minepacu.boothlistmanager.databinding.FragmentHyperlinkgeneratorBinding
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
            hyperLinkGeneratorViewModel.addUpdateLog(
                root, customProgressPage,
                binding.editBoothnameUpdate.text.toString(), binding.editLinkUpdate.text.toString(), binding.editOffsetUpdate.text.toString().toInt())
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

        textWatcher()
        return root
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