package com.nammamela.app.di

import android.content.Context
import androidx.room.Room
import com.nammamela.app.data.local.AppDatabase
import com.nammamela.app.data.remote.ApiService
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://namma-mela-production.up.railway.app/api/")// ⚠️ put your API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "namma_mela_db"
    ).build()

    private val sessionManager = SessionManager(context)

    val repository = AppRepository(apiService, database, sessionManager)
}