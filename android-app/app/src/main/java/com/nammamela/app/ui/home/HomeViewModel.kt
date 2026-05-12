package com.nammamela.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.model.CastMember
import com.nammamela.app.data.model.Play
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _playState = MutableStateFlow<Resource<Play>?>(null)
    val playState: StateFlow<Resource<Play>?> = _playState.asStateFlow()

    private val _castState = MutableStateFlow<Resource<List<CastMember>>?>(null)
    val castState: StateFlow<Resource<List<CastMember>>?> = _castState.asStateFlow()

    init {
        repository.cachedPlay.onEach { cached ->
            cached?.let { _playState.value = Resource.Success(it) }
        }.launchIn(viewModelScope)
        repository.cachedCast.onEach { cached ->
            if (cached.isNotEmpty()) {
                _castState.value = Resource.Success(cached)
            }
        }.launchIn(viewModelScope)
    }

    fun loadHome() {
        viewModelScope.launch {
            _playState.value = Resource.Loading
            _castState.value = Resource.Loading
            _playState.value = repository.fetchPlay()
            _castState.value = repository.fetchCast()
        }
    }
}
