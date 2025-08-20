import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ne_yesem.domain.model.PantryItem
import com.ne_yesem.domain.model.UnitType

// presentation/ui/home/fridge/PantryAdapter.kt
class PantryAdapter(
    private val onPlus: (PantryItem) -> Unit,
    private val onMinus: (PantryItem) -> Unit,
    private val onDelete: (PantryItem) -> Unit
) : ListAdapter<PantryItem, PantryAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<PantryItem>() {
        override fun areItemsTheSame(o: PantryItem, n: PantryItem) = o.id == n.id
        override fun areContentsTheSame(o: PantryItem, n: PantryItem) = o == n
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.pantryName)
        private val qty  = itemView.findViewById<TextView>(R.id.pantryAmount)
        private val del  = itemView.findViewById<ImageView>(R.id.btnDelete)
        private val plus = itemView.findViewById<ImageView>(R.id.addPantry)
        private val minus= itemView.findViewById<ImageView>(R.id.removePantry)

        fun bind(m: PantryItem) {
            val unitText = m.unit.displayName()
            name.text = m.name
            qty.text  = "${m.qty} $unitText"

            del.setOnClickListener  { onDelete(m) }
            plus.setOnClickListener {
                val updated = m.copy(qty = m.qty + 1)
                qty.text = "${updated.qty} $unitText"
                onPlus(updated)
            }
            minus.setOnClickListener{
                if (m.qty > 1) {
                    val updated = m.copy(qty = m.qty - 1)
                    qty.text = "${updated.qty} $unitText"
                    onMinus(updated)
                }
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_pantry_card, p, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}

fun UnitType.displayName(): String = when(this) {
    UnitType.ADET -> "adet"
    UnitType.GR -> "gr"
    UnitType.ML -> "ml"
    UnitType.LT -> "lt"
}
