package com.israelaguilar.prohiringandroid.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDto
import com.israelaguilar.prohiringandroid.databinding.HandymanServiceElementBinding

class HandymanServiceViewHolder(
    private val binding: HandymanServiceElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(handymanService: HandymanServiceDto) {

        binding.apply {
            tvTitle.text = handymanService.title
            tvDescription.text = handymanService.descrip

            val context = binding.root.context
            /*
                        tvPrice.text = context.getString(
                            R.string.price,
                            treeService.price
                        )

                        tvDuration.text = context.getString(
                            R.string.duration,
                            treeService.duration
                        )*/
        }

        // binding.tvTitle.text = treeService.title

        //Con Glide
        Glide.with(binding.root.context)
            .load(handymanService.thumbnail)
            .into(binding.ivThumbnail)

        //Con Picasso
        /*Picasso.get()
            .load(game.thumbnail)
            .into(binding.ivThumbnail)*/


    }
}
