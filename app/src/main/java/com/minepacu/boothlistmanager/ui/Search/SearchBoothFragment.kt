package com.minepacu.boothlistmanager.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.minepacu.boothlistmanager.boothList.BoothsAdapter
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.databinding.FragmentSearchboothBinding

class SearchBoothFragment : Fragment() {

    private var _binding: FragmentSearchboothBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val searchBoothViewModel =
                ViewModelProvider(this).get(SearchBoothViewModel::class.java)

        _binding = FragmentSearchboothBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val boothsAdapter = BoothsAdapter { boothInfo ->  }

        val recyclerView: RecyclerView = binding.recyclerViewSearchResult
        recyclerView.adapter = boothsAdapter

        searchBoothViewModel.boothsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                boothsAdapter.submitList(it as MutableList<BoothInfo>)
            }
        }

        return root
    }

    private fun adapterOnClick(boothInfo: BoothInfo) {
        TODO("Set Activity After Click")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}