package com.minepacu.boothlistmanager.tools.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActibity<P: BasePresenter> : AppCompatActivity() {

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    abstract fun initDependencies()
}