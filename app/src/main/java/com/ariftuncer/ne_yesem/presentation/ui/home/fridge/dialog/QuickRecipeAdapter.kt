package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.domain.model.RecipeItem
import com.bumptech.glide.Glide

class QuickRecipeAdapter :
    ListAdapter<RecipeItem, QuickVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_recipe, parent, false) // basit bir layout
        return QuickVH(v)
    }

    override fun onBindViewHolder(holder: QuickVH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<RecipeItem>() {
            override fun areItemsTheSame(a: RecipeItem, b: RecipeItem) = a.id == b.id
            override fun areContentsTheSame(a: RecipeItem, b: RecipeItem) = a == b
        }
    }
}

 class QuickVH(v: View) : RecyclerView.ViewHolder(v) {
    private val img: ImageView = v.findViewById(R.id.img)
    private val title: TextView = v.findViewById(R.id.title)
    private val likes: TextView = v.findViewById(R.id.likes)
    private val time: TextView = v.findViewById(R.id.time)

    fun bind(it: RecipeItem) {
        title.text = it.title
        likes.text = "0 kişi beğendi"
        time.text = "45 dk."
        Glide.with(img).load(it.image).into(img)
    }
}
