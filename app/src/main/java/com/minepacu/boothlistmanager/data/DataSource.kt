package com.minepacu.boothlistmanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.minepacu.boothlistmanager.data.model.BoothInfo

class DataSource {
    val temp_: BoothInfo = BoothInfo("Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test")

    private var initialBoothList: List<BoothInfo> = listOf(temp_)
    private val boothsLiveData = MutableLiveData(initialBoothList)

    fun addBooth(boothInfo: BoothInfo) {
        val currentList = boothsLiveData.value
        if (currentList == null) {
            boothsLiveData.postValue(listOf(boothInfo))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, boothInfo)
            boothsLiveData.postValue(updatedList)
        }
    }

    fun removeBooth(boothInfo: BoothInfo) {
        val currentList = boothsLiveData.value
        if (currentList != null) {
            val updateedList = currentList.toMutableList()
            updateedList.remove(boothInfo)
            boothsLiveData.postValue(updateedList)
        }
    }

    fun removeAllBoothInfo() {
        val currentList = boothsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.clear()
            boothsLiveData.postValue(updatedList)
        }
    }

    fun getBoothForName(name: String): BoothInfo? {
        boothsLiveData.value?.let { boothInfos ->
            return boothInfos.firstOrNull { it.boothname == name }
        }
        return null
    }

    fun getBoothInfoList(): LiveData<List<BoothInfo>> {
        return boothsLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}