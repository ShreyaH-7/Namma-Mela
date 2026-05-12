package com.nammamela.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentAdminLoginBinding
import com.nammamela.app.ui.auth.AuthViewModel
import com.nammamela.app.ui.auth.AuthViewModelFactory
import com.nammamela.app.MyApplication
import com.nammamela.app.util.Resource
import kotlinx.coroutines.launch

class AdminLoginFragment : Fragment() {

    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MyApplication
        val repository = app.container.repository
        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        binding.btnLoginWithPin.setOnClickListener {
            val pin = binding.etPin.text.toString().trim()
            if (pin.isBlank()) {
                binding.layoutPin.error = getString(R.string.error_admin_pin_required)
            } else {
                binding.layoutPin.error = null
                viewModel.adminLogin(null, null, pin)
            }
        }

        binding.btnLoginWithCredentials.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_admin_credentials_required),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.adminLogin(email, password, null)
            }
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    binding.progressBar.isVisible = state is Resource.Loading

                    when (state) {
                        is Resource.Success -> {
                            viewModel.resetState()
                            findNavController().navigate(R.id.adminDashboardFragment)
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