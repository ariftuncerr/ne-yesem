package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.domain.model.RecipeItem

class RecommendAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<RecipeItem, RecommendAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<RecipeItem>() {
        override fun areItemsTheSame(old: RecipeItem, new: RecipeItem) = old.id == new.id
        override fun areContentsTheSame(old: RecipeItem, new: RecipeItem) = old == new
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val img: ImageView = view.findViewById(R.id.recipeImg)
        private val name: TextView = view.findViewById(R.id.recipeNameTxt)
        private val fav: TextView = view.findViewById(R.id.numberOfFavTxt)
        private val missing: TextView = view.findViewById(R.id.numberOfMissingTxt)
        private val ctg: TextView = view.findViewById(R.id.typeTxt)

        fun bind(item: RecipeItem) {
            name.text = item.title
            fav.text = "${item.likes} kişi favorilerine ekledi"
            missing.text = if (item.missedCount > 0) "${item.missedCount} eksik malzeme var" else "Tüm malzemeler hazır"
            ctg.text = "recipe" // istersen View.GONE yapabilirsin

            Glide.with(img).load(item.image).into(img)

            itemView.setOnClickListener { onClick(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
