package com.israelaguilar.prohiringandroid.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.PopularProjectsRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProject
import com.israelaguilar.prohiringandroid.databinding.FragmentHomeBinding
import com.israelaguilar.prohiringandroid.ui.LoginActivity
import com.israelaguilar.prohiringandroid.ui.adapters.PopularProjectsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var adapter1: PopularProjectsAdapter
    private lateinit var adapter2: PopularProjectsAdapter
    private lateinit var adapter3: PopularProjectsAdapter
    private var popularProjects1 = mutableListOf<PopularProject>()
    private var popularProjects2 = mutableListOf<PopularProject>()
    private var popularProjects3 = mutableListOf<PopularProject>()
    private lateinit var popularProjectsRepository: PopularProjectsRepository
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Configurar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configurar los RecyclerViews con adaptadores independientes
        setupRecyclerView(binding.recyclerView1, popularProjects1).also { adapter1 = it }
        setupRecyclerView(binding.recyclerView2, popularProjects2).also { adapter2 = it }
        setupRecyclerView(binding.recyclerView3, popularProjects3).also { adapter3 = it }

        // Inicializar Retrofit y Repository
        val retrofit = RetrofitHelper().getPopularProjectsRetrofit()
        popularProjectsRepository = PopularProjectsRepository(retrofit)

        // Cargar los proyectos populares
        loadPopularProjects()

        // Configurar el botón de Cerrar sesión
        binding.logoutButton.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, projects: List<PopularProject>): PopularProjectsAdapter {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = PopularProjectsAdapter(projects) { selectedProject ->
            val action = selectedProject.id?.let {
                HomeFragmentDirections.actionFirstFragmentToPopularProjectsDetailFragment(
                    it
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        recyclerView.adapter = adapter
        return adapter
    }


    private fun loadPopularProjects() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = popularProjectsRepository.getPopularProjectsApi().execute()

                if (response.isSuccessful) {
                    response.body()?.let { projects ->
                        // Dividir los proyectos en tres listas independientes
                        val splitIndex = projects.size / 3
                        val list1 = projects.subList(0, splitIndex)
                        val list2 = projects.subList(splitIndex, splitIndex * 2)
                        val list3 = projects.subList(splitIndex * 2, projects.size)

                        // Actualizar las listas de datos
                        popularProjects1.clear()
                        popularProjects2.clear()
                        popularProjects3.clear()

                        popularProjects1.addAll(list1)
                        popularProjects2.addAll(list2)
                        popularProjects3.addAll(list3)

                        withContext(Dispatchers.Main) {
                            adapter1.notifyDataSetChanged()
                            adapter2.notifyDataSetChanged()
                            adapter3.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Exception: ${e.message}")
            }
        }
    }

    private fun logout() {
        // Mostrar un AlertDialog para confirmar la acción
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.logout)) // Usar la cadena de texto "logout" desde strings.xml
        builder.setMessage(getString(R.string.logout_message)) // Mensaje del dialogo, se debe agregar en strings.xml
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->  // Botón positivo
            // Acción de cerrar sesión
            firebaseAuth.signOut()
            // Redirigir al LoginActivity después de cerrar sesión
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish() // Finaliza la actividad actual
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> // Botón negativo
            // Cancelar la acción, solo cerrar el diálogo
            dialog.dismiss()
        }
        // Mostrar el diálogo
        builder.create().show()
    }


}
