package com.minepacu.boothlistmanager.ui.Booth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.minepacu.boothlistmanager.databinding.FragmentAddboothBinding

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

        val textView: TextView = binding.textBoothNumber
        addboothViewModel.boothnumberText.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val text_BoothName_View: TextView = binding.textBoothName
        addboothViewModel.boothnameText.observe(viewLifecycleOwner) {
            text_BoothName_View.text = it
        }

        val text_Genre_View: TextView = binding.textBoothGenre
        addboothViewModel.GenreText.observe(viewLifecycleOwner) {
            text_Genre_View.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}