package com.nammamela.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.model.Play
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _playState = MutableStateFlow<Resource<Play>?>(null)
    val playState: StateFlow<Resource<Play>?> = _playState.asStateFlow()

    val userName: String get() = repository.currentName()
    val isAdmin: Boolean get() = repository.isAdmin()

    init {
        repository.cachedPlay.onEach { cached ->
            cached?.let { _playState.value = Resource.Success(it) }
        }.launchIn(viewModelScope)
    }

    fun loadPlay() {
        viewModelScope.launch {
            _playState.value = Resource.Loading
            _playState.value = repository.fetchPlay()
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onComplete()
        }
    }
}
