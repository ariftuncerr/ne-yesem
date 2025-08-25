package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.domain.model.RecipeItem

class RecommendAdapter(
    private var favoriteIds: Set<Int> = emptySet(),
    private val onCardClick: (Int) -> Unit,
    private val onHeartToggle: (Int, Boolean) -> Unit = { _, _ -> }
) : ListAdapter<RecipeItem, RecommendAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<RecipeItem>() {
        override fun areItemsTheSame(old: RecipeItem, new: RecipeItem) = old.id == new.id
        override fun areContentsTheSame(old: RecipeItem, new: RecipeItem) = old == new
    }

    fun updateFavoriteIds(newFavs: Set<Int>) {
        favoriteIds = newFavs
        notifyDataSetChanged()
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val img: ImageView = view.findViewById(R.id.recipeImg)
        private val name: TextView = view.findViewById(R.id.recipeNameTxt)
        private val favText: TextView = view.findViewById(R.id.numberOfFavTxt)
        private val missing: TextView = view.findViewById(R.id.numberOfMissingTxt)
        private val ctg: TextView = view.findViewById(R.id.typeTxt)
        private val btnLike: ImageButton = view.findViewById(R.id.btnLike)
        private val recipeCard : LinearLayout = view.findViewById(R.id.recipeCard)

        fun bind(item: RecipeItem) {
            name.text = item.title
            favText.text = "${item.likes} kişi favorilerine ekledi"
            missing.text = if (item.missedCount > 0) "${item.missedCount} eksik malzeme var" else "Tüm malzemeler hazır"
            ctg.text = "recipe" // istersen GONE yap
            Glide.with(img).load(item.image).into(img)

            var isFav = favoriteIds.contains(item.id)
            fun renderHeart() {
                btnLike.setImageResource(if (isFav) R.drawable.baseline_favorite_24 else R.drawable.no_fav_24)
            }
            renderHeart()

            // Kart tıklanınca → detay sayfası
            recipeCard.setOnClickListener {
                onCardClick(item.id)
            }

            // Kalp tıklanınca → toggle
            btnLike.setOnClickListener {
                isFav = !isFav
                renderHeart()
                onHeartToggle(item.id, isFav)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}

