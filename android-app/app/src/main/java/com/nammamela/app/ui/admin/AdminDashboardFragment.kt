package com.nammamela.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nammamela.app.R
import com.nammamela.app.databinding.FragmentAdminDashboardBinding

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardManagePlay.setOnClickListener { findNavController().navigate(R.id.managePlayFragment) }
        binding.cardManageCast.setOnClickListener { findNavController().navigate(R.id.manageCastFragment) }
        binding.cardResetSeats.setOnClickListener { findNavController().navigate(R.id.resetSeatsFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
