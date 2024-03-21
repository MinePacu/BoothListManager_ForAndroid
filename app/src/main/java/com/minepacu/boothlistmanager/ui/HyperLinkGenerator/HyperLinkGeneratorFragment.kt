package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.minepacu.boothlistmanager.databinding.FragmentHyperlinkgeneratorBinding

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
        val slideshowViewModel =
            ViewModelProvider(this).get(HyperLinkGeneratorViewModel::class.java)

        _binding = FragmentHyperlinkgeneratorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textWatcher()
        return root
    }

    fun textWatcher() {
        binding.editLink.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.editLink.text!!.isEmpty()) {
                    binding.editLinkLayout.error = "연결할 링크(URL)를 입력해주세요."
                } else {
                    binding.editLinkLayout.error = null
                }
            }
        })
    }
}