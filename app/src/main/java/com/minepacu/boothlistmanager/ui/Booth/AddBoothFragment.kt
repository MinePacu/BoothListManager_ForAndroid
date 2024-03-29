package com.minepacu.boothlistmanager.ui.Booth

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
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.R
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.databinding.FragmentAddboothBinding
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import kotlinx.coroutines.Job

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

        val items = arrayOf("선입금", "통판")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_sheettype, items)

        (binding.selectionSheet.editText as? AutoCompleteTextView)?.setAdapter(adapter)

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

            Log.d("Debug", "BoothInfo : " + boothInfo.toString())
            customProgressPage?.show()

            PythonClass.setVariable("sheetId", prefs.getString("sheetId", ""))
            addboothViewModel.addBoothInfoToSheet(root, customProgressPage, boothInfo)
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