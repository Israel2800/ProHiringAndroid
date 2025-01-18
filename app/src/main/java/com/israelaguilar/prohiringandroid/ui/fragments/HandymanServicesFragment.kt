package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.HandymanServiceRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDto
import com.israelaguilar.prohiringandroid.databinding.FragmentHandymanServicesBinding
import com.israelaguilar.prohiringandroid.ui.adapters.HandymanServicesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HandymanServicesFragment : Fragment() {

    private var _binding: FragmentHandymanServicesBinding? = null
    private val binding get() = _binding!!

    private lateinit var handymanServiceRepository: HandymanServiceRepository
    private lateinit var handymanServicesAdapter: HandymanServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHandymanServicesBinding.inflate(inflater, container, false)

        // Configuración del RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        handymanServicesAdapter = HandymanServicesAdapter(mutableListOf()) { handymanService ->
            // Acción al hacer click en un HandymanService (se puede implementar navegación o mostrar detalles)
        }
        binding.recyclerView.adapter = handymanServicesAdapter

        // Vinculando el botón de retroceso
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Llamada a la API para obtener los datos
        handymanServiceRepository = HandymanServiceRepository(RetrofitHelper().getHandymanServicesRetrofit())
        getHandymanServices()

        return binding.root
    }

    private fun getHandymanServices() {
        handymanServiceRepository.getHandymanServicesApi().enqueue(object :
            Callback<MutableList<HandymanServiceDto>> {
            override fun onResponse(
                call: Call<MutableList<HandymanServiceDto>>,
                response: Response<MutableList<HandymanServiceDto>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Actualizar la lista en el adapter
                        handymanServicesAdapter.handymanServices.clear()
                        handymanServicesAdapter.handymanServices.addAll(it)
                        handymanServicesAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<HandymanServiceDto>>, t: Throwable) {
                // Manejar error en la llamada a la API
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
