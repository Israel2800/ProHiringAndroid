package com.israelaguilar.prohiringandroid.ui.fragments

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.application.ProHiringApp
import com.israelaguilar.prohiringandroid.data.TreeServiceRepository
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import com.israelaguilar.prohiringandroid.databinding.FragmentGamesListBinding
import com.israelaguilar.prohiringandroid.ui.NetworkReceiver
import com.israelaguilar.prohiringandroid.ui.adapters.TreeServicesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesListFragment : Fragment() {

    private var _binding: FragmentGamesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TreeServiceRepository
    private lateinit var networkReceiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as ProHiringApp).repository

        setupRecyclerView()
        loadData()

        // Configurar el receptor de red
        networkReceiver = NetworkReceiver { onConnectionRestored() }
        requireContext().registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun setupRecyclerView() {
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData() {
        binding.pbLoading.visibility = View.VISIBLE

        val call: Call<MutableList<TreeServiceDto>> = repository.getTreeServicesApi()
        call.enqueue(object : Callback<MutableList<TreeServiceDto>> {
            override fun onResponse(
                call: Call<MutableList<TreeServiceDto>>,
                response: Response<MutableList<TreeServiceDto>>
            ) {
                binding.pbLoading.visibility = View.GONE
                response.body()?.let { treeServices ->
                    binding.rvGames.adapter = TreeServicesAdapter(treeServices) { treeService ->
                        treeService.id?.let { id ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, GameDetailFragment.newInstance(id))
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<TreeServiceDto>>, t: Throwable) {
                binding.pbLoading.visibility = View.GONE
            }
        })
    }

    private fun onConnectionRestored() {
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(networkReceiver)
        _binding = null
    }
}
