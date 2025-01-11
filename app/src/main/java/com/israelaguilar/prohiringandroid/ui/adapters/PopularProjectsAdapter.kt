package com.israelaguilar.prohiringandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.israelaguilar.prohiringandroid.R
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProject
import com.israelaguilar.prohiringandroid.databinding.ItemPopularProjectBinding

class PopularProjectsAdapter(private val popularProjects: List<PopularProject>) :
    RecyclerView.Adapter<PopularProjectsAdapter.PopularProjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularProjectsViewHolder {
        val binding = ItemPopularProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularProjectsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularProjectsViewHolder, position: Int) {
        val project = popularProjects[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int = popularProjects.size

    class PopularProjectsViewHolder(private val binding: ItemPopularProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: PopularProject) {
            binding.title.text = project.title
            Glide.with(binding.imageView.context)
                .load(project.thumbnail)
                .placeholder(R.drawable.house)
                .into(binding.imageView)
        }
    }
}
