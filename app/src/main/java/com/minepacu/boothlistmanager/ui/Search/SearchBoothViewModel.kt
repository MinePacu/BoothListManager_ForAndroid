package com.minepacu.boothlistmanager.ui.Search

import android.content.Context
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minepacu.boothlistmanager.data.DataSource
import com.minepacu.boothlistmanager.data.model.BoothInfo

class SearchBoothViewModel(val dataSource: DataSource) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "개발 중....."
    }
    val text: LiveData<String> = _text

    val boothsLiveData = dataSource.getBoothInfoList()

    fun insertBooth(boothInfo: BoothInfo) {
        if (boothInfo == null) {
            return
        }

        dataSource.addBooth(boothInfo)
    }
}

@Suppress("UNCHECKED_CAST")
class BoothInfoListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBoothViewModel::class.java)) {
            return SearchBoothViewModel(
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw illegalDecoyCallException("Unknown ViewModel Class")
    }
}