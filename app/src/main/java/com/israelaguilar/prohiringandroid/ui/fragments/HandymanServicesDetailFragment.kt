package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.remote.HandymanServicesApi
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDetailDto
import com.israelaguilar.prohiringandroid.databinding.FragmentHandymanServicesDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HandymanServicesDetailFragment : Fragment() {

    private var _binding: FragmentHandymanServicesDetailBinding? = null
    private val binding get() = _binding!!

    private val args: HandymanServicesDetailFragmentArgs by navArgs() // Obtener el ID de los argumentos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHandymanServicesDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        // Vinculando el botón de retroceso
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }



        // Obtener el ID del servicio desde los argumentos
        val handymanServiceId = args.handymanServiceId

        // Llamada a la API para obtener los detalles del servicio
        fetchHandymanServiceDetails(handymanServiceId)

        return view
    }

    private fun fetchHandymanServiceDetails(handymanServiceId: String) {
        val retrofit = RetrofitHelper().getHandymanServicesRetrofit()
        val api = retrofit.create(HandymanServicesApi::class.java)

        api.getHandymanServiceDetailApiary(handymanServiceId).enqueue(object :
            Callback<HandymanServiceDetailDto> {
            override fun onResponse(call: Call<HandymanServiceDetailDto>, response: Response<HandymanServiceDetailDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { detail ->
                        // Actualizar la UI con los detalles del servicio
                        binding.titleTextView.text = detail.title
                        Glide.with(requireContext()).load(detail.image).into(binding.imageView)
                        binding.descriptionTextView.text = detail.longDesc
                    }
                }
            }

            override fun onFailure(call: Call<HandymanServiceDetailDto>, t: Throwable) {
                // Manejo de errores
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
