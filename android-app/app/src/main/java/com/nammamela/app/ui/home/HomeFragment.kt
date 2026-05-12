package com.nammamela.app.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.nammamela.app.databinding.FragmentHomeBinding
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by appViewModels()
    private val castAdapter = CastAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCast.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCast.adapter = castAdapter
        observeState()
        viewModel.loadHome()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.playState.collect { state ->
                        binding.progressBar.isVisible = state is Resource.Loading
                        when (state) {
                            is Resource.Success -> {
                                binding.ivPoster.load(state.data.poster)
                                binding.tvTitle.text = state.data.title
                                binding.tvGenre.text = state.data.genre
                                binding.tvDuration.text = state.data.duration
                                binding.tvDescription.text = state.data.description
                            }

                            is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.castState.collect { state ->
                        when (state) {
                            is Resource.Success -> castAdapter.submitList(state.data)
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
        _binding = null
    }
}
