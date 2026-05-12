package com.nammamela.app

import android.app.Application
import com.nammamela.app.data.local.AppDatabase
import com.nammamela.app.data.remote.ApiClient
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.SessionManager

class NammaMelaApplication : Application() {

    lateinit var repository: AppRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)
        val apiService = ApiClient.create(sessionManager)

        repository = AppRepository(
            apiService = apiService,
            appDatabase = database,
            sessionManager = sessionManager
        )
    }
}
