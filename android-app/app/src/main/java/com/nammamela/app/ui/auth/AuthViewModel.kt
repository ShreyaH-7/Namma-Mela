package com.nammamela.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.model.AuthResult
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResult>?>(null)
    val authState: StateFlow<Resource<AuthResult>?> = _authState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = repository.register(name, email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = repository.login(email, password)
        }
    }

    fun adminLogin(email: String?, password: String?, pin: String?) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            _authState.value = repository.adminLogin(email, password, pin)
        }
    }

    fun resetState() {
        _authState.value = null
    }
}