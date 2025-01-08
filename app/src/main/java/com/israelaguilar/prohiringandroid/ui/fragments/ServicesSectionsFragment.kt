package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        /*
        val action = if (isTreeServices) {
            R.id.action_servicesSectionsFragment_to_treeServicesFragment
        } else {
            R.id.action_servicesSectionsFragment_to_handymanServicesFragment
        }
*/


        // Limpiar la pila y navegar al nuevo fragmento sin que se mantengan los anteriores en segundo plano
        //findNavController().navigate(action)

        // Si prefieres usar un fragmento manual, se puede hacer lo siguiente:
        val fragment = if (isTreeServices) GamesListFragment() else GamesListFragment()
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
