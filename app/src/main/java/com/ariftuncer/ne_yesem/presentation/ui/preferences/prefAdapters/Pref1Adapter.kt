package com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ItemPref1Binding

// Model
 data class Pref1Card(val imageRes: Int, val text: String)

class Pref1Adapter(
    private val items: List<Pref1Card>,
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<Pref1Adapter.VH>() {
    private var selectedIndex: Int = -1

    inner class VH(val binding: ItemPref1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pref1Card, isSelected: Boolean) {
            binding.prefImage.setImageResource(item.imageRes)
            binding.prefText.text = item.text
            val ctx = binding.root.context
            val secondary = ContextCompat.getColor(ctx, R.color.secondary)
            val white = ContextCompat.getColor(ctx, android.R.color.white)
            val textColor = ContextCompat.getColor(ctx, R.color.text950)
            val defaultBg = ContextCompat.getColor(ctx, android.R.color.white)
            binding.cardView.setCardBackgroundColor(if (isSelected) secondary else defaultBg)
            binding.prefText.setTextColor(if (isSelected) white else textColor)
            binding.root.setOnClickListener {
                onCardClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        val binding = ItemPref1Binding.inflate(inf, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], position == selectedIndex)
    }

    override fun getItemCount() = items.size

    fun setSelected(position: Int) {
        val old = selectedIndex
        selectedIndex = position
        notifyItemChanged(old)
        notifyItemChanged(selectedIndex)
    }
}

