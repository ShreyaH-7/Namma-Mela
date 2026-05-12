package com.nammamela.app.ui.auth

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
import com.nammamela.app.databinding.FragmentAuthBinding
import com.nammamela.app.util.Resource
import com.nammamela.app.util.Validation
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ CORRECT WAY (using AppContainer)
        val app = requireActivity().application as com.nammamela.app.MyApplication
        val repository = app.container.repository

        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setupViews()
        observeState()
    }

    private fun setupViews() {
        binding.switchAuth.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutName.isVisible = isChecked
            binding.btnPrimary.text =
                if (isChecked) getString(R.string.register) else getString(R.string.login)
            binding.tvModeTitle.text =
                if (isChecked) getString(R.string.create_account)
                else getString(R.string.login_to_continue)
        }

        binding.btnPrimary.setOnClickListener {
            if (binding.switchAuth.isChecked) register() else login()
        }

        binding.btnAdminLogin.setOnClickListener {
            findNavController().navigate(R.id.adminLoginFragment)
        }
    }

    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (name.isBlank()) {
            binding.layoutName.error = getString(R.string.error_name_required)
            return
        } else binding.layoutName.error = null

        if (!Validation.isEmailValid(email)) {
            binding.layoutEmail.error = getString(R.string.error_invalid_email)
            return
        } else binding.layoutEmail.error = null

        if (!Validation.isPasswordValid(password)) {
            binding.layoutPassword.error = getString(R.string.error_invalid_password)
            return
        } else binding.layoutPassword.error = null

        viewModel.register(name, email, password)
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (!Validation.isEmailValid(email)) {
            binding.layoutEmail.error = getString(R.string.error_invalid_email)
            return
        } else binding.layoutEmail.error = null

        if (!Validation.isPasswordValid(password)) {
            binding.layoutPassword.error = getString(R.string.error_invalid_password)
            return
        } else binding.layoutPassword.error = null

        viewModel.login(email, password)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    binding.progressBar.isVisible = state is Resource.Loading

                    when (state) {
                        is Resource.Success -> {
                            viewModel.resetState()
                            findNavController().navigate(R.id.dashboardFragment)
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