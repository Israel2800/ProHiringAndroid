package com.israelaguilar.prohiringandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDto
import com.israelaguilar.prohiringandroid.databinding.HandymanServiceElementBinding

class HandymanServicesAdapter(
    val handymanServices: MutableList<HandymanServiceDto>,
    private val onHandymanSerivceClicked: (HandymanServiceDto) -> Unit
): RecyclerView.Adapter<HandymanServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandymanServiceViewHolder {
        val binding = HandymanServiceElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HandymanServiceViewHolder(binding)
    }

    override fun getItemCount(): Int = handymanServices.size

    override fun onBindViewHolder(holder: HandymanServiceViewHolder, position: Int) {

        val handymanService = handymanServices[position]

        holder.bind(handymanService)

        holder.itemView.setOnClickListener {
            //Para los clicks a los servicios
            onHandymanSerivceClicked(handymanService)
        }

    }

}
