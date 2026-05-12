package com.nammamela.app.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.repository.AppRepository
import com.nammamela.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val _reservedSeatsState = MutableStateFlow<Resource<List<String>>?>(null)
    val reservedSeatsState: StateFlow<Resource<List<String>>?> = _reservedSeatsState.asStateFlow()

    private val _bookingState = MutableStateFlow<Resource<Unit>?>(null)
    val bookingState: StateFlow<Resource<Unit>?> = _bookingState.asStateFlow()

    fun loadSeats() {
        viewModelScope.launch {
            _reservedSeatsState.value = Resource.Loading
            _reservedSeatsState.value = repository.fetchSeats()
        }
    }

    fun bookSeats(customerName: String, seats: List<String>) {
        viewModelScope.launch {
            _bookingState.value = Resource.Loading
            _bookingState.value = repository.createBooking(customerName, seats)
            if (_bookingState.value is Resource.Success) {
                loadSeats()
            }
        }
    }

    fun clearBookingState() {
        _bookingState.value = null
    }

    fun clearReservedSeatsState() {
        _reservedSeatsState.value = null
    }
}
