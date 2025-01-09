package com.israelaguilar.prohiringandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.db.model.Company
import com.israelaguilar.prohiringandroid.databinding.FragmentCompaniesBinding
import com.israelaguilar.prohiringandroid.databinding.ItemCompanyBinding

class CompaniesFragment : Fragment() {

    private lateinit var companiesRecyclerView: RecyclerView
    private lateinit var companiesAdapter: CompanyAdapter
    private var companies: List<Company> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCompaniesBinding.inflate(inflater, container, false)

        companiesRecyclerView = binding.companiesRecyclerView
        companiesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Cargar datos de compañías
        loadCompaniesData()

        return binding.root
    }

    private fun loadCompaniesData() {
        // Aquí puedes cargar los datos desde Firebase o cualquier otra fuente
        FirebaseFirestore.getInstance().collection("companies")
            .get()
            .addOnSuccessListener { result ->
                companies = result.map { document ->
                    Company(
                        companyName = document.getString("companyName") ?: "Unknown",
                        services = document.getString("services") ?: "No services",
                        logoURL = document.getString("logoURL") ?: ""
                    )
                }

                companiesAdapter = CompanyAdapter(companies)
                companiesRecyclerView.adapter = companiesAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("CompaniesFragment", "Error getting documents.", exception)
            }
    }

    // Adapter para el RecyclerView
    class CompanyAdapter(private val companies: List<Company>) :
        RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
            val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CompanyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
            val company = companies[position]
            holder.bind(company)
        }

        override fun getItemCount() = companies.size

        // ViewHolder
        class CompanyViewHolder(private val binding: ItemCompanyBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(company: Company) {
                binding.companyName.text = company.companyName
                binding.services.text = company.services
                loadLogoImage(company.logoURL, binding.logoImageView)
            }

            private fun loadLogoImage(url: String, imageView: ImageView) {
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            }
        }
    }
}
