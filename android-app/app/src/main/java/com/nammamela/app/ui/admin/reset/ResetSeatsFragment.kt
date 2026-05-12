package com.nammamela.app.ui.admin.reset

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
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentResetSeatsBinding
import com.nammamela.app.ui.admin.AdminViewModel
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class ResetSeatsFragment : Fragment() {

    private var _binding: FragmentResetSeatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResetSeatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnResetSeats.setOnClickListener {
            viewModel.resetSeats()
        }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resetState.collect { state ->
                    binding.progressBar.isVisible = state is Resource.Loading
                    binding.btnResetSeats.isEnabled = state !is Resource.Loading
                    when (state) {
                        is Resource.Success -> {
                            Toast.makeText(requireContext(), state.data, Toast.LENGTH_LONG).show()
                            viewModel.clearResetState()
                        }
                        is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
