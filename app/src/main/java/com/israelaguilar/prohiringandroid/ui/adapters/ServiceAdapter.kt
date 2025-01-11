package com.israelaguilar.prohiringandroid.ui.adapters

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
            itemView.findViewById<TextView>(R.id.serviceName).text = service.name
            itemView.findViewById<TextView>(R.id.serviceStatus).text = service.status
            itemView.setOnClickListener { onItemClick(service) }
        }
    }
}
