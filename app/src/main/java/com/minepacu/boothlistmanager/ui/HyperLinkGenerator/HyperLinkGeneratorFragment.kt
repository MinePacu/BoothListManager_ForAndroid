package com.minepacu.boothlistmanager.ui.HyperLinkGenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.filledCopyToClipBoardButton.setOnClickListener {
            val copiedstring = "=HYPERLINK(\"" + binding.editLink.text + "\", \"" + binding.editLinklabel.text + "\")"
            textCopyThenPost(copiedstring)
        }

        textWatcher()
        return root
    }

    fun textWatcher() {
        binding.editLink.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    fun textCopyThenPost(textCopied: String) {
        val clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, textCopied + " 복사됨", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}