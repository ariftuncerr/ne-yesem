package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.databinding.ItemRecipeBinding
import com.ariftuncer.ne_yesem.domain.model.RecipeDetail
import com.bumptech.glide.Glide

class LastViewsAdapter(
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<LastViewsAdapter.LastViewHolder>() {
    private val items = mutableListOf<RecipeDetail>()

    fun submitList(newItems: List<RecipeDetail>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class LastViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeDetail) {
            binding.recipeNameTxt.text = item.title
            Glide.with(binding.recipeImg.context).load(item.image).into(binding.recipeImg)
            binding.durationTxt.text = item.readyInMinutes?.let { "$it dk." } ?: "-"
            binding.numberOfFavTxt.visibility = View.GONE
            binding.numberOfMissingTxt.visibility = View.GONE
            binding.typeTxt.visibility = View.GONE
            binding.recipeCard.setOnClickListener { onCardClick(item.id) }
        }
    }
}

