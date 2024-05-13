package com.minepacu.boothlistmanager.ui.Search

import android.content.Context
import android.view.View
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.minepacu.boothlistmanager.data.DataSource
import com.minepacu.boothlistmanager.data.model.BoothInfo
import com.minepacu.boothlistmanager.tools.PythonCode.PythonClass
import com.minepacu.boothlistmanager.ui.ProgressingPage.ProgressPage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchBoothViewModel(val dataSource: DataSource) : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "개발 중....."
    }
    val text: LiveData<String> = _text

    */

    val boothsLiveData = dataSource.getBoothInfoList()

    fun insertBooth(boothInfo: BoothInfo) {
        if (boothInfo == null) {
            return
        }

        dataSource.addBooth(boothInfo)
    }

    fun removeAllBoothInfo() {
        dataSource.removeAllBoothInfo()
    }

    /**
     * 지정한 매개 변수를 검색어로 활용하여 부스 인포를 검색한 후, [RecyclerView]에 출력합니다.
     *
     * @param view 함수의 실행 결과를 출력하는 [Snackbar]를 출력할 [View] 객체
     * @param processingRing 함수가 실행되는 동안, 출력할 로딩 창 ([ProgressPage]) 객체
     * @param boothnumber 검색어로 쓸 부스 번호
     * @param boothname 검색어로 쓸 부스 이름
     * @param boothgenre 검색어로 쓸 장르
     */
    fun searchBoothInfo(view: View, progressPage: ProgressPage?, boothnumber: String? = null, boothname: String? = null, boothgenre: String? = null, authorNickName: String? = null) : Job {
        return viewModelScope.launch {
            removeAllBoothInfo()
            val result = PythonClass.searchBoothInfo(boothnumber, boothname, boothgenre)

            when {
                result != null -> {
                    insertBooth(result)
                    Snackbar.make(view, "검색이 완료되었습니다.",
                        Snackbar.LENGTH_LONG)
                        .show()
                    progressPage?.hide()
                }
                else -> {
                    Snackbar.make(
                        view, "검색에 오류가 있습니다.",
                        Snackbar.LENGTH_LONG)
                        .show()
                    progressPage?.hide()
                }
            }
        }
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