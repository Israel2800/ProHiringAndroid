package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.israelaguilar.prohiringandroid.data.TreeServiceRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.databinding.FragmentTreeServicesBinding
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import com.israelaguilar.prohiringandroid.ui.adapters.TreeServicesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TreeServicesFragment : Fragment() {

    private var _binding: FragmentTreeServicesBinding? = null
    private val binding get() = _binding!!

    private lateinit var treeServiceRepository: TreeServiceRepository
    private lateinit var treeServicesAdapter: TreeServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreeServicesBinding.inflate(inflater, container, false)

        // Configuración del RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        treeServicesAdapter = TreeServicesAdapter(mutableListOf()) { treeService ->
            // Aquí navegamos al TreeServicesDetailFragment y pasamos el ID
            val action = treeService.id?.let {
                TreeServicesFragmentDirections.actionTreeServicesFragmentToTreeServicesDetailFragment(
                    it // Pasamos el id
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.recyclerView.adapter = treeServicesAdapter

        // Llamada a la API para obtener los datos
        treeServiceRepository = TreeServiceRepository(RetrofitHelper().getTreeServicesRetrofit())
        getTreeServices()

        return binding.root
    }

    private fun getTreeServices() {
        treeServiceRepository.getTreeServicesApi().enqueue(object : Callback<MutableList<TreeServiceDto>> {
            override fun onResponse(
                call: Call<MutableList<TreeServiceDto>>,
                response: Response<MutableList<TreeServiceDto>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Actualizar la lista en el adapter
                        treeServicesAdapter.treeServices.clear()
                        treeServicesAdapter.treeServices.addAll(it)
                        treeServicesAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<TreeServiceDto>>, t: Throwable) {
                // Manejar error en la llamada a la API
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
