package com.minepacu.boothlistmanager.ui.Booth

import android.app.ProgressDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val customProgressPage = this.context?.let { ProgressPage(it) }

        customProgressPage?.window?.setBackgroundDrawable(
            ColorDrawable(android.graphics.Color.TRANSPARENT))

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

            var new_yoil : String = ""
            when {
                yoil == binding.FesInSaturday.id -> new_yoil = "토"
                yoil == binding.FesInSunday.id -> new_yoil = "일"
                yoil == binding.FesInboth.id -> new_yoil = "토/일"
            }

            val boothInfo = BoothInfo(boothnumber, boothname, genre, new_yoil,
                infolabel, infolink,
                preorder_date, preorder_label, preorder_link)

            customProgressPage?.show()
            addboothViewModel.addBoothInfoToSheet(root, boothInfo)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}