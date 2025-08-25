package com.ariftuncer.ne_yesem.presentation.ui.home.homepages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.databinding.ItemPopularBinding
import com.ariftuncer.ne_yesem.domain.model.DishType

data class PopularCategoryUi(
    val type: DishType,
    val displayNameTr: String,
    @DrawableRes val imageRes: Int
)

class CategoryAdapter(
    private val onClick: (PopularCategoryUi) -> Unit
) : ListAdapter<PopularCategoryUi, CategoryAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<PopularCategoryUi>() {
        override fun areItemsTheSame(o: PopularCategoryUi, n: PopularCategoryUi) = o.type == n.type
        override fun areContentsTheSame(o: PopularCategoryUi, n: PopularCategoryUi) = o == n
    }

    inner class VH(private val b: ItemPopularBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: PopularCategoryUi) = with(b) {
            popularCardText.text = item.displayNameTr
            popularCategoryImg.setImageResource(item.imageRes)
            popularCard1.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
