package com.israelaguilar.prohiringandroid.ui.fragments

import android.content.Intent
import android.net.Uri
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.db.model.Company
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

class CompanyProfileFragment : Fragment() {

    private lateinit var company: Company
    private lateinit var backButton: ImageButton
    private lateinit var contactLabel: TextView
    private lateinit var emailLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_profile, container, false)

        backButton = view.findViewById(R.id.backButton)

        // Acción para el botón de regresar
        backButton.setOnClickListener {
            findNavController().navigateUp()  // Usar el NavController en lugar de onBackPressed()
        }

        company = arguments?.getSerializable("company") as Company

        val companyNameLabel: TextView = view.findViewById(R.id.companyNameLabel)
        val logoImageView: ImageView = view.findViewById(R.id.logoImageView)
        val servicesLabel: TextView = view.findViewById(R.id.servicesDataLabel)
        val socialMediaLabel: TextView = view.findViewById(R.id.socialMediaDataLabel)
        contactLabel = view.findViewById(R.id.contactDataLabel)
        emailLabel = view.findViewById(R.id.emailDataLabel)

        companyNameLabel.text = company.companyName
        servicesLabel.text = company.services
        socialMediaLabel.text = company.socialMedia
        contactLabel.text = company.contact
        emailLabel.text = company.name

        // Cargar el logo
        if (!company.logoURL.isEmpty()) {
            Glide.with(this)
                .load(company.logoURL)
                .placeholder(R.drawable.house)
                .into(logoImageView)
        }

        setupGestureRecognizers()

        return view
    }


    private fun setupGestureRecognizers() {
        contactLabel.setOnClickListener {
            handleContactTap()
        }

        emailLabel.setOnClickListener {
            handleEmailTap()
        }
    }


    private fun handleContactTap() {
        company?.contact?.let { phoneNumber ->
            val phoneUri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_DIAL, phoneUri)
            startActivity(intent)
        } ?: run {
            showToast("Número de contacto no disponible")
        }
    }

    private fun handleEmailTap() {
        company?.name?.let { email ->
            val emailUri = Uri.parse("mailto:$email")
            val intent = Intent(Intent.ACTION_SENDTO, emailUri)
            startActivity(intent)
        } ?: run {
            showToast("Correo no disponible")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}



