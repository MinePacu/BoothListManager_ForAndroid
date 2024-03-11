package com.minepacu.boothlistmanager.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.minepacu.boothlistmanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val RQ_GOOGLE_SIGN_IN = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textServiceConnection
        homeViewModel.text_ServiceConnection.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val textView_ServiceConnectionStatus: TextView = binding.textServiceConnectionStatus
        homeViewModel.text_ServiceConnectionStatus.observe(viewLifecycleOwner) {
            textView_ServiceConnectionStatus.text = it
        }
        return root
    }

    fun launchAuthentication(client: GoogleSignInClient) {
        startActivityForResult(client.signInIntent, RQ_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RQ_GOOGLE_SIGN_IN = 999
    }
}