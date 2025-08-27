package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ItemRecipeBinding
import com.ariftuncer.ne_yesem.domain.model.DishTypeRecipe
import com.bumptech.glide.Glide

class DishTypeAdapter(
    private var favoriteIds: Set<Int> = emptySet(),
    private val selectedDishTypeApiName: String,              // <-- EKLENDİ
    private val onCardClick: (DishTypeRecipe) -> Unit,
    private val onHeartToggle: (recipeId: Int, nowFavorite: Boolean) -> Unit
) : ListAdapter<DishTypeRecipe, DishTypeAdapter.VH>(DIFF) {


    fun updateFavoriteIds(newFavs: Set<Int>) {
        favoriteIds = newFavs
        notifyDataSetChanged()
    }

    object DIFF : DiffUtil.ItemCallback<DishTypeRecipe>() {
        override fun areItemsTheSame(o: DishTypeRecipe, n: DishTypeRecipe) = o.id == n.id
        override fun areContentsTheSame(o: DishTypeRecipe, n: DishTypeRecipe) = o == n
    }

    inner class VH(val b: ItemRecipeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: DishTypeRecipe) = with(b) {
            recipeNameTxt.text = item.title
            durationTxt.text = item.readyInMinutes?.let { "$it dk." } ?: "—"
            numberOfFavTxt.text = item.likes?.let { "$it kişi favori" } ?: ""
            numberOfFavTxt.visibility = if (item.likes == null) View.GONE else View.VISIBLE
            Glide.with(recipeImg).load(item.image).into(recipeImg)

            val st = styleFor(root.context, selectedDishTypeApiName)
            typeTxt.text = st.label                   // ← yalnızca seçilen kategori adı
            typeTxt.setTextColor(st.text)
            typeTxt.setBackgroundResource(R.drawable.chip_category)
            androidx.core.view.ViewCompat.setBackgroundTintList(
                typeTxt, android.content.res.ColorStateList.valueOf(st.bg)
            )


            var isFav = favoriteIds.contains(item.id)
            fun renderHeart() {
                btnLike.setImageResource(if (isFav) R.drawable.baseline_favorite_24 else R.drawable.no_fav_24)
            }
            renderHeart()

            // Kalp → favori toggle
            btnLike.setOnClickListener {
                isFav = !isFav
                renderHeart()
                onHeartToggle(item.id, isFav)
            }

            // Kart → detaya git
            recipeCard.setOnClickListener { onCardClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    private data class DishTypeStyle(val label: String, @androidx.annotation.ColorInt val text: Int, @androidx.annotation.ColorInt val bg: Int)

    private fun styleFor(ctx: android.content.Context, api: String?): DishTypeStyle {
        return when (api) {
            "breakfast"   -> DishTypeStyle("Kahvaltı",
                ctx.getColor(R.color.colorBreakfast), ctx.getColor(R.color.bgBreakfast))
            "main course" -> DishTypeStyle("Ana Yemek",
                ctx.getColor(R.color.colorMainCourse), ctx.getColor(R.color.bgMainCourse))
            "salad"       -> DishTypeStyle("Salata",
                ctx.getColor(R.color.colorSalad), ctx.getColor(R.color.bgSalad))
            "dessert"     -> DishTypeStyle("Tatlı",
                ctx.getColor(R.color.colorDessert), ctx.getColor(R.color.bgDessert))
            "sauce"       -> DishTypeStyle("Sos",
                ctx.getColor(R.color.colorSauce), ctx.getColor(R.color.bgSauce))
            "bread"       -> DishTypeStyle("Hamurişi",
                ctx.getColor(R.color.colorPastry), ctx.getColor(R.color.bgPastry))
            "snack"       -> DishTypeStyle("Atıştırmalık",
                ctx.getColor(R.color.colorSnack), ctx.getColor(R.color.bgSnack))
            "appetizer"   -> DishTypeStyle("Meze",
                ctx.getColor(R.color.colorAppetizer), ctx.getColor(R.color.bgAppetizer))
            "beverage"    -> DishTypeStyle("İçecek",
                ctx.getColor(R.color.colorDrink), ctx.getColor(R.color.bgDrink))
            "soup"        -> DishTypeStyle("Çorba",
                ctx.getColor(R.color.colorAppetizer), ctx.getColor(R.color.bgAppetizer)) // varsa özel renk koy
            else -> DishTypeStyle("Tarif", ctx.getColor(R.color.text600), ctx.getColor(R.color.primary50))
        }
    }

}
