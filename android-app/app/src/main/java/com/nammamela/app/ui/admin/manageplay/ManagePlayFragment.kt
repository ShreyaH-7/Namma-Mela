package com.nammamela.app.ui.admin.manageplay

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
import com.nammamela.app.data.remote.dto.PlayRequest
import com.nammamela.app.databinding.FragmentManagePlayBinding
import com.nammamela.app.ui.admin.AdminViewModel
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class ManagePlayFragment : Fragment() {

    private var _binding: FragmentManagePlayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManagePlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSavePlay.setOnClickListener {
            val request = PlayRequest(
                title = binding.etTitle.text.toString().trim(),
                genre = binding.etGenre.text.toString().trim(),
                duration = binding.etDuration.text.toString().trim(),
                description = binding.etDescription.text.toString().trim(),
                poster = binding.etPoster.text.toString().trim()
            )

            if (!validate(request)) {
                return@setOnClickListener
            } else {
                viewModel.savePlay(request)
            }
        }
        observeState()
        viewModel.loadPlay()
    }

    private fun validate(request: PlayRequest): Boolean {
        binding.layoutTitle.error = if (request.title.isBlank()) getString(R.string.error_title_required) else null
        binding.layoutGenre.error = if (request.genre.isBlank()) getString(R.string.error_genre_required) else null
        binding.layoutDuration.error = if (request.duration.isBlank()) getString(R.string.error_duration_required) else null
        binding.layoutDescription.error = if (request.description.isBlank()) getString(R.string.error_description_required) else null
        binding.layoutPoster.error = if (request.poster.isBlank()) getString(R.string.error_poster_required) else null

        return listOf(
            binding.layoutTitle.error,
            binding.layoutGenre.error,
            binding.layoutDuration.error,
            binding.layoutDescription.error,
            binding.layoutPoster.error
        ).all { it == null }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.playState.collect { state ->
                        when (state) {
                            is Resource.Success -> {
                                if (binding.etTitle.text.isNullOrBlank()) binding.etTitle.setText(state.data.title)
                                if (binding.etGenre.text.isNullOrBlank()) binding.etGenre.setText(state.data.genre)
                                if (binding.etDuration.text.isNullOrBlank()) binding.etDuration.setText(state.data.duration)
                                if (binding.etDescription.text.isNullOrBlank()) binding.etDescription.setText(state.data.description)
                                if (binding.etPoster.text.isNullOrBlank()) binding.etPoster.setText(state.data.poster)
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.playSaveState.collect { state ->
                        binding.progressBar.isVisible = state is Resource.Loading
                        binding.btnSavePlay.isEnabled = state !is Resource.Loading
                        when (state) {
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), getString(R.string.play_updated), Toast.LENGTH_LONG).show()
                                viewModel.clearPlaySaveState()
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
        _binding = null
    }
}
