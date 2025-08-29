package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R

class StepsAdapter :
    ListAdapter<String, StepsAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(o: String, n: String) = o === n
        override fun areContentsTheSame(o: String, n: String) = o == n
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val card: CardView = view as CardView
        private val text: TextView = view.findViewById(R.id.text1)
        fun bind(stepText: String, index: Int) { text.text = "${index + 1}. $stepText" }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_step_card, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position), position)
}
