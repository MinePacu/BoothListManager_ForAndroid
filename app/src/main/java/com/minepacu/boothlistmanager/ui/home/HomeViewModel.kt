package com.minepacu.boothlistmanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text_ServiceConnection = MutableLiveData<String>().apply {
        value = "서비스 연결 상태"
    }
    val text_ServiceConnection: LiveData<String> = _text_ServiceConnection

    private val _text_ServiceConnectionStatus = MutableLiveData<String>().apply {
        value = "서비스 상태"
    }
    val text_ServiceConnectionStatus: LiveData<String> = _text_ServiceConnectionStatus
}