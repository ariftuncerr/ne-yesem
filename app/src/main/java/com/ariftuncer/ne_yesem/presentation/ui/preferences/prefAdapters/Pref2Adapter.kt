package com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ItemTagCardBinding

data class AllergenTag(
    val label: String, val selected: Boolean = false
)

class Pref2Adapter(
    private val onToggle: (position: Int) -> Unit
) : ListAdapter<AllergenTag, Pref2Adapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<AllergenTag>() {
            override fun areItemsTheSame(o: AllergenTag, n: AllergenTag) = o.label.equals(n.label, true)
            override fun areContentsTheSame(o: AllergenTag, n: AllergenTag) = o == n
        }
    }

    inner class VH(val b: ItemTagCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: AllergenTag) {
            val ctx = b.root.context
            val secondary = ContextCompat.getColor(ctx, R.color.secondary) // veya attr ile çözüyorsan değiştir
            val white = ContextCompat.getColor(ctx, android.R.color.white)
            val transparent = ContextCompat.getColor(ctx, android.R.color.transparent)

            // Arka plan & metin rengi
            b.card.setCardBackgroundColor(if (item.selected) secondary else transparent)
            b.txt.setTextColor(if (item.selected) white else secondary)

            // ✓ ikon görünürlüğü
            b.check.visibility = if (item.selected) View.VISIBLE else View.GONE
            if (item.selected) b.check.setColorFilter(white)

            // Tıklayınca toggle
            b.card.setOnClickListener { onToggle(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        val binding = ItemTagCardBinding.inflate(inf, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.b.txt.text = getItem(position).label
        holder.bind(getItem(position))
    }
}
