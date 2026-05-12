package com.nammamela.app.ui.fanwall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.data.model.Comment
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FanWallViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _commentState = MutableStateFlow<Resource<List<Comment>>?>(null)
    val commentState: StateFlow<Resource<List<Comment>>?> = _commentState.asStateFlow()

    private val _addCommentState = MutableStateFlow<Resource<Unit>?>(null)
    val addCommentState: StateFlow<Resource<Unit>?> = _addCommentState.asStateFlow()

    init {
        repository.cachedComments.onEach { cached ->
            if (cached.isNotEmpty()) {
                _commentState.value = Resource.Success(cached)
            }
        }.launchIn(viewModelScope)
    }

    fun loadComments() {
        viewModelScope.launch {
            _commentState.value = Resource.Loading
            _commentState.value = repository.fetchComments()
        }
    }

    fun addComment(name: String, message: String) {
        viewModelScope.launch {
            _addCommentState.value = Resource.Loading
            _addCommentState.value = repository.addComment(name, message)
            if (_addCommentState.value is Resource.Success) {
                loadComments()
            }
        }
    }

    fun clearAddCommentState() {
        _addCommentState.value = null
    }
}
