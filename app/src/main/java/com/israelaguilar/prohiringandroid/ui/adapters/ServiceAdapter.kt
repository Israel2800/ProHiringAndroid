package com.israelaguilar.prohiringandroid.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.db.model.Service

class ServiceAdapter(private val services: List<Service>, private val onItemClick: (Service) -> Unit) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_item, parent, false)
        return ServiceViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.bind(service)
    }

    override fun getItemCount() = services.size

    class ServiceViewHolder(itemView: View, private val onItemClick: (Service) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(service: Service) {
            val serviceName = itemView.findViewById<TextView>(R.id.serviceName)
            val serviceStatus = itemView.findViewById<TextView>(R.id.serviceStatus)

            // Asignar el nombre del servicio
            serviceName.text = service.name
            serviceStatus.text = "Status: ${service.color}"

            // Cambiar el color de fondo de la celda según el color del servicio
            val colorCode = when (service.color) {
                "Searching a Pro" -> "#00FF00" // Rojo
                "Currently working" -> "#FFFF00" // Amarillo
                "Job done" -> "#FF0000" // Verde
                else -> "#FFFFFF" // Blanco (por defecto si no hay color definido)
            }
            itemView.setBackgroundColor(Color.parseColor(colorCode))

            // Asegurarse de que el texto sea siempre negro
            serviceName.setTextColor(Color.BLACK)
            serviceStatus.setTextColor(Color.BLACK)

            itemView.setOnClickListener { onItemClick(service) }
        }
    }
}
