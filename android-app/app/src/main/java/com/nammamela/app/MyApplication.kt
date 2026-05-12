package com.nammamela.app

import android.app.Application
import com.nammamela.app.di.AppContainer

class MyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
