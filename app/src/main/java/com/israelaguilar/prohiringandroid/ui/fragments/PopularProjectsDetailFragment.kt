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
import com.israelaguilar.prohiringandroid.data.remote.PopularProjectsApi
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProjectDetailDto
import com.israelaguilar.prohiringandroid.databinding.FragmentPopularProjectsDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularProjectsDetailFragment : Fragment() {

    private var _binding: FragmentPopularProjectsDetailBinding? = null
    private val binding get() = _binding!!

    private val args: PopularProjectsDetailFragmentArgs by navArgs() // Obtener el ID de los argumentos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularProjectsDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        // Vinculando el botón de retroceso
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }



        // Obtener el ID del servicio desde los argumentos
        val popularProjectId = args.popularProjectId

        // Llamada a la API para obtener los detalles del servicio
        fetchPopularProjectDetails(popularProjectId)

        return view
    }

    private fun fetchPopularProjectDetails(popularProjectId: String) {
        val retrofit = RetrofitHelper().getPopularProjectsRetrofit()
        val api = retrofit.create(PopularProjectsApi::class.java)

        api.getPopularProjectDetailApiary(popularProjectId).enqueue(object :
            Callback<PopularProjectDetailDto> {
            override fun onResponse(call: Call<PopularProjectDetailDto>, response: Response<PopularProjectDetailDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { detail ->
                        // Actualizar la UI con los detalles del servicio
                        binding.titleTextView.text = detail.title
                        Glide.with(requireContext()).load(detail.image).into(binding.imageView)
                        binding.descriptionTextView.text = detail.longDesc
                    }
                }
            }

            override fun onFailure(call: Call<PopularProjectDetailDto>, t: Throwable) {
                // Manejo de errores
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
