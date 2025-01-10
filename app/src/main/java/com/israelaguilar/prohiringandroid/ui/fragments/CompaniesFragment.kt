package com.example.prohiring.ui.fragments

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.db.model.Company
import com.israelaguilar.prohiringandroid.ui.fragments.CompanyProfileFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CompaniesFragment : Fragment() {

    private lateinit var companiesRecyclerView: RecyclerView
    private val companies = mutableListOf<Company>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_companies, container, false)

        companiesRecyclerView = view.findViewById(R.id.companiesRecyclerView)
        companiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        companiesRecyclerView.adapter = CompaniesAdapter(companies) { company ->
            navigateToCompanyProfile(company)
        }

        loadCompaniesData()
        return view
    }

    private fun loadCompaniesData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("companies").get()
            .addOnSuccessListener { snapshot ->
                companies.clear()
                snapshot?.documents?.forEach { document ->
                    val data = document.data ?: return@forEach
                    val company = Company(
                        companyName = data["companyName"] as? String ?: "Unknown",
                        services = data["services"] as? String ?: "No services",
                        logoURL = data["logoURL"] as? String ?: "",
                        socialMedia = data["socialMedia"] as? String ?: "No social media",
                        contact = data["contact"] as? String ?: "No contact info",
                        name = data["name"] as? String ?: "No email"
                    )
                    companies.add(company)
                }
                companiesRecyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("CompaniesFragment", "Error loading companies: ${e.message}")
            }
    }

    private fun navigateToCompanyProfile(company: Company) {
        val bundle = Bundle().apply {
            putSerializable("company", company)
        }

        // Navegar usando el NavController
        findNavController().navigate(
            R.id.companyProfileFragmentSelected,
            bundle
        )
    }


    class CompaniesAdapter(private val companies: List<Company>, private val onClick: (Company) -> Unit) :
        RecyclerView.Adapter<CompaniesAdapter.CompanyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_company, parent, false)
            return CompanyViewHolder(view)
        }

        override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
            val company = companies[position]
            holder.bind(company, onClick)
        }

        override fun getItemCount(): Int = companies.size

        class CompanyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val companyName: TextView = view.findViewById(R.id.companyName)
            private val services: TextView = view.findViewById(R.id.services)
            private val logoImageView: ImageView = view.findViewById(R.id.logoImageView)

            fun bind(company: Company, onClick: (Company) -> Unit) {
                companyName.text = company.companyName
                services.text = company.services
                Glide.with(logoImageView.context)
                    .load(company.logoURL)
                    .placeholder(R.drawable.house)
                    .into(logoImageView)

                itemView.setOnClickListener {
                    onClick(company)
                }
            }
        }
    }
}
