package com.minepacu.boothlistmanager.ui.Booth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddBoothViewModel : ViewModel() {

    private val _boothnumberText = MutableLiveData<String>().apply {
        value = "부스 번호"
    }
    val boothnumberText: LiveData<String> = _boothnumberText



    private val _boothnameText = MutableLiveData<String>().apply {
        value = "부스 이름"
    }
    val boothnameText: LiveData<String> = _boothnameText

    private val _GenreText = MutableLiveData<String>().apply {
        value = "부스 장르"
    }
    val GenreText: LiveData<String> = _GenreText
}