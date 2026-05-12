package com.nammamela.app.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentBookingBinding
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookingViewModel by appViewModels()
    private lateinit var seatAdapter: SeatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seatAdapter = SeatAdapter { selected ->
            binding.tvSelectedSeats.text = selected.ifBlank { getString(R.string.no_seats_selected) }
        }
        binding.recyclerSeats.layoutManager = GridLayoutManager(requireContext(), 20)
        binding.recyclerSeats.adapter = seatAdapter

        binding.btnBookNow.setOnClickListener {
            val name = binding.etCustomerName.text.toString().trim()
            val selectedSeats = seatAdapter.getSelectedSeats()
            when {
                name.isBlank() -> binding.layoutCustomerName.error = getString(R.string.error_customer_name_required)
                selectedSeats.isEmpty() -> Toast.makeText(requireContext(), getString(R.string.error_select_seat), Toast.LENGTH_LONG).show()
                else -> {
                    binding.layoutCustomerName.error = null
                    viewModel.bookSeats(name, selectedSeats)
                }
            }
        }

        observeState()
        viewModel.loadSeats()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.reservedSeatsState.collect { state ->
                        binding.progressBar.isVisible = state is Resource.Loading
                        binding.btnBookNow.isEnabled = state !is Resource.Loading && viewModel.bookingState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> seatAdapter.updateReservedSeats(state.data)
                            is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.bookingState.collect { state ->
                        binding.btnBookNow.isEnabled = state !is Resource.Loading && viewModel.reservedSeatsState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), getString(R.string.booking_success), Toast.LENGTH_LONG).show()
                                binding.etCustomerName.text?.clear()
                                binding.tvSelectedSeats.text = getString(R.string.no_seats_selected)
                                seatAdapter.clearSelectedSeats()
                                viewModel.clearBookingState()
                            }
                            is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearReservedSeatsState()
        _binding = null
    }
}
