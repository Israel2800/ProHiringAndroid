package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.israelaguilar.prohiringandroid.data.PopularProjectsRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProject
import com.israelaguilar.prohiringandroid.databinding.FragmentHomeBinding
import com.israelaguilar.prohiringandroid.ui.adapters.PopularProjectsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularProjectsAdapter
    private var popularProjects = mutableListOf<PopularProject>()
    private lateinit var popularProjectsRepository: PopularProjectsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        adapter = PopularProjectsAdapter(popularProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Inicializar Retrofit y Repository
        val retrofit = RetrofitHelper().getPopularProjectsRetrofit()
        popularProjectsRepository = PopularProjectsRepository(retrofit)

        // Cargar los proyectos populares
        loadPopularProjects()

        return binding.root
    }

    private fun loadPopularProjects() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Realizar la llamada a la API para obtener los proyectos populares
                val response = popularProjectsRepository.getPopularProjectsApi().execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        // Limpiar la lista de proyectos y agregar los nuevos
                        popularProjects.clear()
                        popularProjects.addAll(it)

                        // Actualizar la interfaz de usuario en el hilo principal
                        withContext(Dispatchers.Main) {
                            adapter.notifyDataSetChanged()
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
}
