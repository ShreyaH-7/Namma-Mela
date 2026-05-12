package com.nammamela.app.ui.main

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
import androidx.navigation.fragment.findNavController
import coil.load
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentDashboardBinding
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvWelcome.text = getString(R.string.welcome_user, viewModel.userName)
        binding.cardAdmin.isVisible = viewModel.isAdmin

        binding.cardHome.setOnClickListener { findNavController().navigate(R.id.homeFragment) }
        binding.cardBooking.setOnClickListener { findNavController().navigate(R.id.bookingFragment) }
        binding.cardFanWall.setOnClickListener { findNavController().navigate(R.id.fanWallFragment) }
        binding.cardAdmin.setOnClickListener { findNavController().navigate(R.id.adminDashboardFragment) }
        binding.btnLogout.setOnClickListener {
            viewModel.logout {
                findNavController().navigate(R.id.authFragment)
            }
        }

        observeState()
        viewModel.loadPlay()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playState.collect { state ->
                    binding.progressBar.isVisible = state is Resource.Loading
                    when (state) {
                        is Resource.Success -> {
                            binding.tvPlayTitle.text = state.data.title
                            binding.tvPlayGenre.text = state.data.genre
                            binding.tvPlayDuration.text = state.data.duration
                            binding.tvPlayDescription.text = state.data.description
                            binding.ivPoster.load(state.data.poster)
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        }

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
