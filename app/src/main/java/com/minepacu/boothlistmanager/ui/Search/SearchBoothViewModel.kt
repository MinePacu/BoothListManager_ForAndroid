package com.minepacu.boothlistmanager.ui.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchBoothViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "개발 중....."
    }
    val text: LiveData<String> = _text
}