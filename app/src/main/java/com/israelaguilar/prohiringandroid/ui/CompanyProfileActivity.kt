package com.israelaguilar.prohiringandroid.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.israelaguilar.prohiringandroid.R
import com.squareup.picasso.Picasso

class CompanyProfileActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var companyNameLabel: TextView
    private lateinit var servicesLabel: TextView
    private lateinit var socialMediaLabel: TextView
    private lateinit var contactLabel: TextView
    private lateinit var emailLabel: TextView
    private lateinit var logoutButton: Button

    private var companyId: String? = null  // ID de la compañía, debe ser pasado desde la actividad anterior

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)

        // Inicialización de vistas
        logoImageView = findViewById(R.id.logoImageView)
        companyNameLabel = findViewById(R.id.companyNameLabel)
        servicesLabel = findViewById(R.id.servicesDataLabel)
        socialMediaLabel = findViewById(R.id.socialMediaDataLabel)
        contactLabel = findViewById(R.id.contactDataLabel)
        emailLabel = findViewById(R.id.emailDataLabel)
        logoutButton = findViewById(R.id.logoutButton)

        // Recibir el ID de la compañía (pasado desde la actividad anterior)
        companyId = intent.getStringExtra("COMPANY_ID")

        companyId?.let { loadCompanyProfile(it) }

        // Configurar el botón de cierre de sesión
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun loadCompanyProfile(companyId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("companies").document(companyId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val companyName = document.getString("companyName")
                    val services = document.getString("services")
                    val socialMedia = document.getString("socialMedia")
                    val contact = document.getString("contact")
                    val email = document.getString("name")
                    val logoURL = document.getString("logoURL")

                    // Mostrar los datos en las vistas
                    companyNameLabel.text = companyName
                    servicesLabel.text = services
                    socialMediaLabel.text = socialMedia
                    contactLabel.text = contact
                    emailLabel.text = email

                    // Cargar el logo
                    if (!logoURL.isNullOrEmpty()) {
                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(logoURL)
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            Picasso.get().load(uri).into(logoImageView)
                        }.addOnFailureListener {
                            Toast.makeText(this, "Error cargando el logo", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Company not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar los datos de la compañía", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()  // Cerrar sesión
        Toast.makeText(this, "Has cerrado sesión correctamente", Toast.LENGTH_SHORT).show()
        finish()  // Volver a la pantalla de inicio o actividad previa
    }
}
