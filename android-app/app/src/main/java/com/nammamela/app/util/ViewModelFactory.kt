package com.nammamela.app.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.ui.admin.AdminViewModel
import com.nammamela.app.ui.auth.AuthViewModel
import com.nammamela.app.ui.booking.BookingViewModel
import com.nammamela.app.ui.fanwall.FanWallViewModel
import com.nammamela.app.ui.home.HomeViewModel
import com.nammamela.app.ui.main.DashboardViewModel
import com.nammamela.app.ui.splash.SplashViewModel

class ViewModelFactory(
    private val repository: AppRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(repository) as T
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(BookingViewModel::class.java) -> BookingViewModel(repository) as T
            modelClass.isAssignableFrom(FanWallViewModel::class.java) -> FanWallViewModel(repository) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) -> AdminViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
