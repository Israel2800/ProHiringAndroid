package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.databinding.FragmentServicesSectionsBinding

class ServicesSectionsFragment : Fragment() {

    private var _binding: FragmentServicesSectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServicesSectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSections()
    }

    private fun setupSections() {
        binding.treeServicesSection.setOnClickListener {
            navigateToServicesList(true)
        }

        binding.handymanServicesSection.setOnClickListener {
            navigateToServicesList(false)
        }
    }

    private fun navigateToServicesList(isTreeServices: Boolean) {
        val fragment = if (isTreeServices) {
            GamesListFragment()
        } else {
            GamesListFragment()
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
