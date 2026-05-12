package com.nammamela.app.ui.fanwall

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
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentFanWallBinding
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class FanWallFragment : Fragment() {

    private var _binding: FragmentFanWallBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FanWallViewModel by appViewModels()
    private val adapter = CommentAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFanWallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerComments.adapter = adapter

        binding.btnAddComment.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val message = binding.etMessage.text.toString().trim()
            when {
                name.isBlank() -> binding.layoutName.error = getString(R.string.error_name_required)
                message.isBlank() -> binding.layoutMessage.error = getString(R.string.error_message_required)
                else -> {
                    binding.layoutName.error = null
                    binding.layoutMessage.error = null
                    viewModel.addComment(name, message)
                }
            }
        }

        observeState()
        viewModel.loadComments()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.commentState.collect { state ->
                        binding.progressBar.isVisible = state is Resource.Loading
                        binding.btnAddComment.isEnabled = state !is Resource.Loading && viewModel.addCommentState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> adapter.submitList(state.data)
                            is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.addCommentState.collect { state ->
                        binding.btnAddComment.isEnabled = state !is Resource.Loading && viewModel.commentState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> {
                                binding.etName.text?.clear()
                                binding.etMessage.text?.clear()
                                Toast.makeText(requireContext(), getString(R.string.comment_added), Toast.LENGTH_SHORT).show()
                                viewModel.clearAddCommentState()
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
