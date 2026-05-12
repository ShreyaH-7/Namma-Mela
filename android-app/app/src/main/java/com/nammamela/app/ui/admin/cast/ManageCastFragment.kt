package com.nammamela.app.ui.admin.cast

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
import com.nammamela.app.data.remote.dto.CastRequest
import com.nammamela.app.databinding.FragmentManageCastBinding
import com.nammamela.app.ui.admin.AdminViewModel
import com.nammamela.app.ui.home.CastAdapter
import com.nammamela.app.util.Resource
import com.nammamela.app.util.appViewModels
import kotlinx.coroutines.launch

class ManageCastFragment : Fragment() {

    private var _binding: FragmentManageCastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by appViewModels()
    private val castAdapter = CastAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManageCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCast.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCast.adapter = castAdapter
        binding.btnAddCast.setOnClickListener {
            val request = CastRequest(
                name = binding.etName.text.toString().trim(),
                role = binding.etRole.text.toString().trim(),
                bio = binding.etBio.text.toString().trim(),
                image = binding.etImage.text.toString().trim()
            )

            if (!validate(request)) {
                return@setOnClickListener
            } else {
                viewModel.addCast(request)
            }
        }
        observeState()
        viewModel.loadCast()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.castState.collect { state ->
                        binding.progressBar.isVisible = state is Resource.Loading
                        binding.btnAddCast.isEnabled = state !is Resource.Loading && viewModel.castSaveState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> castAdapter.submitList(state.data)
                            is Resource.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.castSaveState.collect { state ->
                        binding.btnAddCast.isEnabled = state !is Resource.Loading && viewModel.castState.value !is Resource.Loading
                        when (state) {
                            is Resource.Success -> {
                                binding.etName.text?.clear()
                                binding.etRole.text?.clear()
                                binding.etBio.text?.clear()
                                binding.etImage.text?.clear()
                                Toast.makeText(requireContext(), getString(R.string.cast_member_added), Toast.LENGTH_LONG).show()
                                viewModel.clearCastSaveState()
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

    private fun validate(request: CastRequest): Boolean {
        binding.layoutName.error = if (request.name.isBlank()) getString(R.string.error_name_required) else null
        binding.layoutRole.error = if (request.role.isBlank()) getString(R.string.error_role_required) else null
        binding.layoutBio.error = if (request.bio.isBlank()) getString(R.string.error_bio_required) else null
        binding.layoutImage.error = if (request.image.isBlank()) getString(R.string.error_image_required) else null

        return listOf(
            binding.layoutName.error,
            binding.layoutRole.error,
            binding.layoutBio.error,
            binding.layoutImage.error
        ).all { it == null }
    }
}
