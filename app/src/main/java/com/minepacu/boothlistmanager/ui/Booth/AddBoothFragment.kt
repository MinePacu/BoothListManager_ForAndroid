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
            customProgressPage?.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}