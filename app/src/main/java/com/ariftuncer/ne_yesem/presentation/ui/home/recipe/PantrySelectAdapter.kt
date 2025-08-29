import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.domain.model.PantryItem

// presentation/ui/home/recipe/adapter/PantrySelectAdapter.kt
class PantrySelectAdapter(
    private val onToggle: (PantryItem, Boolean) -> Unit
) : ListAdapter<PantryItem, PantrySelectAdapter.VH>(DIFF) {

    private val selectedIds = mutableSetOf<String>()

    fun setAllSelected(items: List<PantryItem>, selected: Boolean) {
        selectedIds.clear()
        if (selected) selectedIds.addAll(items.map { it.id })
        notifyDataSetChanged()
    }

    fun getSelected(): List<String> = currentList
        .filter { selectedIds.contains(it.id) }
        .map { it.name } // öneri çağrısı için isim listesi

    override fun onCreateViewHolder(p: ViewGroup, vt: Int) = VH(
        LayoutInflater.from(p.context).inflate(R.layout.item_pantry, p, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(currentList[pos])

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.addPantryTxt)
        private val check = view.findViewById<ImageView>(R.id.checkBtn)
        private val add = view.findViewById<ImageView>(R.id.addBtn)
        private val card = view as CardView

        init {
            // bu ekranda "add" ikonunu kullanmıyoruz
            add.visibility = View.GONE
            itemView.setOnClickListener {
                val bindingAdapterPosition = 0
                val item = currentList[bindingAdapterPosition]
                val nowSel = toggle(item.id)
                reflect(nowSel)

                onToggle(item, nowSel)
            }
        }

        fun bind(item: PantryItem) {
            title.text = item.name
            reflect(selectedIds.contains(item.id))
        }

        private fun toggle(id: String): Boolean {
            if (!selectedIds.add(id)) selectedIds.remove(id)
            return selectedIds.contains(id)
        }

        private fun reflect(isSelected: Boolean) {
            check.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
            // seçili görünüm: primary/10 arka plan + stroke
            val ctx = itemView.context
            card.setCardBackgroundColor(
                if (isSelected) ContextCompat.getColor(ctx, R.color.secondary)
                else ContextCompat.getColor(ctx, android.R.color.white)
            )
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PantryItem>() {
            override fun areItemsTheSame(o: PantryItem, n: PantryItem) = o.id == n.id
            override fun areContentsTheSame(o: PantryItem, n: PantryItem) = o == n
        }
    }
}
