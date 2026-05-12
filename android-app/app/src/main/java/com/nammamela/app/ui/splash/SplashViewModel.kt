package com.nammamela.app.ui.splash

import androidx.lifecycle.ViewModel
import com.nammamela.app.repository.AppRepository

class SplashViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    fun isLoggedIn(): Boolean = repository.isLoggedIn()
}
