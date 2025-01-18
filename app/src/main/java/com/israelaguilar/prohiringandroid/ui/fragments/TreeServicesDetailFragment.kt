package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper
import com.israelaguilar.prohiringandroid.data.remote.TreeServicesApi
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDetailDto
import com.israelaguilar.prohiringandroid.databinding.FragmentTreeServicesDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TreeServicesDetailFragment : Fragment() {

    private var _binding: FragmentTreeServicesDetailBinding? = null
    private val binding get() = _binding!!

    private val args: TreeServicesDetailFragmentArgs by navArgs() // Obtener el ID de los argumentos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreeServicesDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        // Vinculando el botón de retroceso
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }



        // Obtener el ID del servicio desde los argumentos
        val treeServiceId = args.treeServiceId

        // Llamada a la API para obtener los detalles del servicio
        fetchTreeServiceDetails(treeServiceId)

        return view
    }

    private fun fetchTreeServiceDetails(treeServiceId: String) {
        val retrofit = RetrofitHelper().getTreeServicesRetrofit()
        val api = retrofit.create(TreeServicesApi::class.java)

        api.getTreeServiceDetailApiary(treeServiceId).enqueue(object : Callback<TreeServiceDetailDto> {
            override fun onResponse(call: Call<TreeServiceDetailDto>, response: Response<TreeServiceDetailDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { detail ->
                        // Actualizar la UI con los detalles del servicio
                        binding.titleTextView.text = detail.title
                        Glide.with(requireContext()).load(detail.image).into(binding.imageView)
                        binding.descriptionTextView.text = detail.longDesc
                    }
                }
            }

            override fun onFailure(call: Call<TreeServiceDetailDto>, t: Throwable) {
                // Manejo de errores
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
