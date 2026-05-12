package com.nammamela.app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.model.CastMember
import com.nammamela.app.data.model.Play
import com.nammamela.app.data.remote.dto.CastRequest
import com.nammamela.app.data.remote.dto.PlayRequest
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _playSaveState = MutableStateFlow<Resource<Play>?>(null)
    val playSaveState: StateFlow<Resource<Play>?> = _playSaveState.asStateFlow()

    private val _castSaveState = MutableStateFlow<Resource<Unit>?>(null)
    val castSaveState: StateFlow<Resource<Unit>?> = _castSaveState.asStateFlow()

    private val _resetState = MutableStateFlow<Resource<String>?>(null)
    val resetState: StateFlow<Resource<String>?> = _resetState.asStateFlow()

    private val _castState = MutableStateFlow<Resource<List<CastMember>>?>(null)
    val castState: StateFlow<Resource<List<CastMember>>?> = _castState.asStateFlow()

    private val _playState = MutableStateFlow<Resource<Play>?>(null)
    val playState: StateFlow<Resource<Play>?> = _playState.asStateFlow()

    init {
        repository.cachedCast.onEach { cached ->
            if (cached.isNotEmpty()) {
                _castState.value = Resource.Success(cached)
            }
        }.launchIn(viewModelScope)

        repository.cachedPlay.onEach { cached ->
            cached?.let {
                _playState.value = Resource.Success(it)
            }
        }.launchIn(viewModelScope)
    }

    fun savePlay(request: PlayRequest) {
        viewModelScope.launch {
            _playSaveState.value = Resource.Loading
            _playSaveState.value = repository.savePlay(request)
        }
    }

    fun loadPlay() {
        viewModelScope.launch {
            _playState.value = Resource.Loading
            _playState.value = repository.fetchPlay()
        }
    }

    fun loadCast() {
        viewModelScope.launch {
            _castState.value = Resource.Loading
            _castState.value = repository.fetchCast()
        }
    }

    fun addCast(request: CastRequest) {
        viewModelScope.launch {
            _castSaveState.value = Resource.Loading
            _castSaveState.value = repository.addCast(request)
            if (_castSaveState.value is Resource.Success) {
                loadCast()
            }
        }
    }

    fun resetSeats() {
        viewModelScope.launch {
            _resetState.value = Resource.Loading
            _resetState.value = repository.resetSeats()
        }
    }

    fun clearPlaySaveState() {
        _playSaveState.value = null
    }

    fun clearCastSaveState() {
        _castSaveState.value = null
    }

    fun clearResetState() {
        _resetState.value = null
    }
}
