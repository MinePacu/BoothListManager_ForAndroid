package com.minepacu.boothlistmanager.ui.Booth

import android.app.ProgressDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.compose.material3.Snackbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.databinding.FragmentAddboothBinding
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage

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
            binding.editPreOrderDate.editText?.setText("")
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
            val preorder_date = binding.editPreOrderDate.editText?.text.toString()
            val preorder_label = binding.editPreOrderLabel.editText?.text.toString()
            val preorder_link = binding.editPreOrderLink.editText?.text.toString()

            var new_yoil  = ""
            when {
                yoil == binding.FesInSaturday.id -> new_yoil = "토"
                yoil == binding.FesInSunday.id -> new_yoil = "일"
                yoil == binding.FesInboth.id -> new_yoil = "토/일"
            }

            val boothInfo = BoothInfo(boothnumber, boothname, genre, new_yoil,
                infolabel, infolink,
                preorder_date, preorder_label, preorder_link)

            customProgressPage?.show()
            val result = addboothViewModel.addBoothInfoToSheet(root, boothInfo)
            if (result.isCompleted) {
                customProgressPage?.hide()
            } else {
                customProgressPage?.hide()
                Snackbar.make(root, "부스 정보가 추가되지 못했습니다.", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        textWatcher()
        return root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}