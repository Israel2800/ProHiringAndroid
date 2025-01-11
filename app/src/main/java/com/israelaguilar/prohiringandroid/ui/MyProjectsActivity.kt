package com.israelaguilar.prohiringandroid.ui

import android.widget.Toast
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.db.model.Service
import com.israelaguilar.prohiringandroid.ui.adapters.ServiceAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyProjectsActivity : AppCompatActivity() {

    private lateinit var serviceTableView: RecyclerView
    private lateinit var serviceNameTextField: EditText
    private lateinit var emailLabel: TextView
    private lateinit var serviceColorPicker: Spinner
    private lateinit var addServiceButton: Button
    private lateinit var deleteServiceButton: Button

    private lateinit var serviceAdapter: ServiceAdapter
    private var serviceList = mutableListOf<Service>()
    private val colors = arrayOf("Searching a Pro", "Currently working", "Job done")

    private val db = FirebaseFirestore.getInstance()
    private var selectedService: Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_projects)

        // Inicializar componentes
        serviceTableView = findViewById(R.id.serviceTableView)
        serviceNameTextField = findViewById(R.id.serviceNameTextField)
        emailLabel = findViewById(R.id.emailLabel)
        serviceColorPicker = findViewById(R.id.serviceColorPicker)
        addServiceButton = findViewById(R.id.addServiceButton)
        deleteServiceButton = findViewById(R.id.deleteServiceButton)

        // Configurar RecyclerView
        serviceTableView.layoutManager = LinearLayoutManager(this)
        serviceAdapter = ServiceAdapter(serviceList) { service ->
            editService(service)
        }
        serviceTableView.adapter = serviceAdapter

        // Configurar Spinner
        val colorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        serviceColorPicker.adapter = colorAdapter

        // Mostrar email del usuario actual
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            emailLabel.text = it.email
            loadUserServices(it.uid)
        }

        // Configurar botones
        addServiceButton.setOnClickListener {
            if (selectedService == null) {
                addService()
            } else {
                updateService(selectedService!!)
            }
        }

        deleteServiceButton.setOnClickListener {
            deleteService()
        }
    }

    private fun loadUserServices(userId: String) {
        db.collection("users")
            .document(userId)
            .collection("services")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("MyProjectsActivity", "Error al escuchar cambios", error)
                    return@addSnapshotListener
                }

                serviceList.clear()
                snapshot?.documents?.forEach { document ->
                    val service = document.toObject(Service::class.java)
                    service?.id = document.id // Asignar el ID del documento al servicio
                    service?.let { serviceList.add(it) }
                }
                serviceAdapter.notifyDataSetChanged()
            }
    }

    private fun addService() {
        val serviceName = serviceNameTextField.text.toString()
        val serviceColor = serviceColorPicker.selectedItem.toString()

        if (serviceName.isEmpty()) {
            showError("Por favor ingrese el nombre del servicio.")
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newService = Service(
            id = "",
            name = serviceName,
            status = "New",
            color = serviceColor
        )

        db.collection("users")
            .document(userId)
            .collection("services")
            .add(newService)
            .addOnSuccessListener {
                serviceNameTextField.text.clear()
                loadUserServices(userId)
            }
            .addOnFailureListener {
                showError("Error al agregar el servicio.")
            }
    }

    private fun editService(service: Service) {
        selectedService = service
        serviceNameTextField.setText(service.name)
        serviceColorPicker.setSelection(colors.indexOf(service.color))
        addServiceButton.text = "Edit Service"
    }

    private fun updateService(service: Service) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val updatedService = Service(
            id = service.id,
            name = serviceNameTextField.text.toString(),
            status = "Updated",
            color = serviceColorPicker.selectedItem.toString()
        )

        db.collection("users")
            .document(userId)
            .collection("services")
            .document(service.id)
            .set(updatedService)
            .addOnSuccessListener {
                serviceNameTextField.text.clear()
                selectedService = null
                addServiceButton.text = "Add Service"
                loadUserServices(userId)
            }
            .addOnFailureListener {
                showError("Error al actualizar el servicio.")
            }
    }

    private fun deleteService() {
        selectedService?.let { service ->
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            db.collection("users")
                .document(userId)
                .collection("services")
                .document(service.id)
                .delete()
                .addOnSuccessListener {
                    loadUserServices(userId)
                }
                .addOnFailureListener {
                    showError("Error al eliminar el servicio.")
                }
        } ?: showError("No hay un servicio seleccionado.")
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
