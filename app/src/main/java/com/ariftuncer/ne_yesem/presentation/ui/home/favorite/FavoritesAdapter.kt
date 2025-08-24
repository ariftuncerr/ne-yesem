package com.ariftuncer.ne_yesem.presentation.ui.home.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ItemRecipeBinding
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard
import com.bumptech.glide.Glide

class FavoritesAdapter(
    private val onClick: (FavoriteRecipeCard) -> Unit,
    private val onHeart: (FavoriteRecipeCard) -> Unit
) : ListAdapter<FavoriteRecipeCard, FavoritesAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<FavoriteRecipeCard>() {
        override fun areItemsTheSame(o: FavoriteRecipeCard, n: FavoriteRecipeCard) = o.id == n.id
        override fun areContentsTheSame(o: FavoriteRecipeCard, n: FavoriteRecipeCard) = o == n
    }

    inner class VH(val b: ItemRecipeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: FavoriteRecipeCard) = with(b) {
            recipeNameTxt.text = item.title
            durationTxt.text = item.readyInMinutes?.let { "$it dk." } ?: "—"
            typeTxt.text = item.dishTypes?.firstOrNull() ?: "Tarif"
            numberOfFavTxt.text = item.likes?.let { "$it kişi favori" } ?: ""
            numberOfFavTxt.visibility = if (item.likes == null) View.GONE else View.VISIBLE

            Glide.with(recipeImg).load(item.image).into(recipeImg)

            // Favoriler ekranında tümü favori → dolu kalp
            btnLike.setImageResource(R.drawable.baseline_favorite_24)
            btnLike.setOnClickListener { onHeart(item) }

            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
