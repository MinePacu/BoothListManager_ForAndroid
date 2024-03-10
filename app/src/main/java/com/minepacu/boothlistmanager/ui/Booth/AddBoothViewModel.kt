package com.minepacu.boothlistmanager.ui.Booth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddBoothViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "부스 이름"
    }
    val text: LiveData<String> = _text
}