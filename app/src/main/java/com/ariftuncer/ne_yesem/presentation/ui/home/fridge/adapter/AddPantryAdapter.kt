package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ne_yesem.domain.model.PantryItem

class AddPantryAdapter(
    private val items: List<PantryItem>,
    private val selectedItems: MutableSet<String>,
    private val onItemSelected: (PantryItem, Boolean) -> Unit
) : RecyclerView.Adapter<AddPantryAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.addPantryTxt)
        private val checkLayout = itemView.findViewById<View>(R.id.checkLayout)
        private val card = itemView as androidx.cardview.widget.CardView

        fun bind(item: PantryItem, isSelected: Boolean) {
            name.text = item.name
            checkLayout.visibility = if (isSelected) View.VISIBLE else View.GONE
            card.setCardBackgroundColor(
                ContextCompat.getColor(
                    card.context,
                    if (isSelected) R.color.secondary else android.R.color.white
                )
            )
            card.setOnClickListener {
                val newState = !isSelected
                onItemSelected(item, newState)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pantry, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val isSelected = selectedItems.contains(item.id)
        holder.bind(item, isSelected)
    }

    fun selectAll() {
        selectedItems.clear()
        items.forEach { selectedItems.add(it.id) }
        notifyDataSetChanged()
    }
}
