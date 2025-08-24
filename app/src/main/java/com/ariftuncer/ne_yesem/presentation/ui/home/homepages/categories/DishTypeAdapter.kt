import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.databinding.ItemRecipeBinding
import com.ariftuncer.ne_yesem.domain.model.DishTypeRecipe
import com.bumptech.glide.Glide

class DishTypeAdapter(
    private val onClick: (DishTypeRecipe) -> Unit
) : ListAdapter<DishTypeRecipe, DishTypeAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<DishTypeRecipe>() {
        override fun areItemsTheSame(o: DishTypeRecipe, n: DishTypeRecipe) = o.id == n.id
        override fun areContentsTheSame(o: DishTypeRecipe, n: DishTypeRecipe) = o == n
    }

    inner class VH(val b: ItemRecipeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: DishTypeRecipe) = with(b) {
            recipeNameTxt.text = item.title
            durationTxt.text = item.readyInMinutes?.let { "$it dk." } ?: "—"
            typeTxt.text = item.dishTypes?.joinToString(", ") ?: "—"
            numberOfFavTxt.text = item.likes?.let { "$it kişi favori" } ?: ""
            numberOfFavTxt.visibility = if (item.likes == null) View.GONE else View.VISIBLE

            Glide.with(recipeImg).load(item.image).into(recipeImg)

            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
