package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.domain.model.PantryItem

class AddPantryAdapter(
    private val items: List<PantryItem>,
    private val selectedItems: MutableSet<String>,
    private val onItemSelected: (PantryItem, Boolean) -> Unit,
    private val onCardClick: (PantryItem) -> Unit   // 👈 YENİ
) : RecyclerView.Adapter<AddPantryAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.addPantryTxt)
        private val checkLayout = itemView.findViewById<View>(R.id.checkLayout)
        private val card = itemView as androidx.cardview.widget.CardView

        // AddPantryAdapter.kt  → VH.bind(...)
        fun bind(item: PantryItem, isSelected: Boolean) {
            name.text = item.name
            checkLayout.visibility = if (isSelected) View.VISIBLE else View.GONE

            // ➊ Kart arka planını secondary yap
            val secondary = com.google.android.material.color.MaterialColors.getColor(
                itemView, com.google.android.material.R.attr.colorSecondary
            )
            (itemView as CardView).setCardBackgroundColor(secondary)

            // ➋ Metin rengini onSecondary yap (okunabilirlik)
            val onSecondary = com.google.android.material.color.MaterialColors.getColor(
                itemView, com.google.android.material.R.attr.colorOnSecondary
            )
            name.setTextColor(onSecondary)

            // ➌ Davranışlar aynı
            itemView.setOnClickListener { onCardClick(item) }
            itemView.setOnLongClickListener {
                val ns = !isSelected
                onItemSelected(item, ns)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pantry, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.bind(it, selectedItems.contains(it.id))
    }

    fun selectAll() {
        selectedItems.clear()
        items.forEach { selectedItems.add(it.id) }
        notifyDataSetChanged()
    }
}
