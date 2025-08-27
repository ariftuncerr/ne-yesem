package com.ariftuncer.ne_yesem.presentation.ui.home.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentFavoriteBinding
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard
import com.ariftuncer.ne_yesem.presentation.ui.home.favorite.sheet.FilterBottomSheet
import com.ariftuncer.ne_yesem.presentation.ui.home.favorite.sheet.SortBottomSheet
import com.ariftuncer.ne_yesem.presentation.viewmodel.FavoritesViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _b: FragmentFavoriteBinding? = null
    private val b get() = _b!!
    private val vm: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

    // ARAMA / FİLTRE / SIRALAMA durumu
    private var query: String = ""
    private val tr = Locale("tr", "TR")
    private var allCards: List<FavoriteRecipeCard> = emptyList()
    private var activeFilters: Set<FilterOption> = emptySet()
    private var activeSort: SortOption = SortOption.NEWEST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentFavoriteBinding.inflate(inflater, container, false)

        // Liste kaynağını tek yerden dinle → Arama/Filtre/Sıralama boru hattına aktar
        vm.cards.observe(viewLifecycleOwner) { list ->
            allCards = list
            applyFilterAndSort()
            b.noRecipeLayout.isVisible = list.isEmpty()
            b.rvFavorites.isVisible = list.isNotEmpty()
        }

        // Bottom sheet tetikleyicileri
        b.sortBtn.setOnClickListener {
            SortBottomSheet(activeSort) { selected ->
                activeSort = selected
                applyFilterAndSort()
            }.show(parentFragmentManager, "SortBS")
        }
        b.filterBtn.setOnClickListener {
            FilterBottomSheet(activeFilters) { selected ->
                activeFilters = selected
                applyFilterAndSort()
            }.show(parentFragmentManager, "FilterBS")
        }

        // Arama
        b.searchFavoriteRecipeEditTxt.doOnTextChanged { text, _, _, _ ->
            query = text?.toString().orEmpty()
            applyFilterAndSort()
        }

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComponents()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        adapter = FavoritesAdapter(
            onClick = { card ->
                // NAVIGATION: Favorite → RecipeDetail (Bundle ile)
                // Eğer action id'in varsa R.id.action_favoriteFragment_to_recipeDetailFragment kullan:
                findNavController().navigate(
                    R.id.recipeDetailFragment,
                    bundleOf("recipeId" to card.id)
                )
            },
            onHeart = { card ->
                vm.toggle(uid, card.id, nowFavorite = false)
                // UI’dan hızlı kaldır
                val newList = currentList().filterNot { it.id == card.id }
                adapter.submitList(newList)
            }
        )

        b.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvFavorites.adapter = adapter

        // ❌ 2. observer’ı kaldırdım; filtre/arama sonucunu ezmesin
        vm.refresh(uid)
    }

    private fun currentList() = adapter.currentList.toList()

    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Favoriler"
        // resource id yerine gerçek renk int’i verelim (uyumlu olur)
        try {
            toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.primary950))
        } catch (_: Exception) { /* eski davranış kalsın */ }
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    // --- Arama + Filtre + Sıralama ---
    private fun applyFilterAndSort() {
        var list = allCards

        // 1) Arama
        list = list.filter { matchesQuery(it) }

        // 2) Filtre
        if (activeFilters.isNotEmpty()) {
            val labels = activeFilters.map { it.label.lowercase(tr) }.toSet()
            list = list.filter { card ->
                val types = (card.dishTypes ?: emptyList()).map { it.lowercase(tr) }
                types.any { it in labels }
            }
        }

        // 3) Sıralama
        list = when (activeSort) {
            SortOption.NEWEST -> list
            SortOption.OLDEST -> list
            SortOption.ALPHA_AZ -> list.sortedBy { it.title ?: "" }
            SortOption.ALPHA_ZA -> list.sortedByDescending { it.title ?: "" }
            SortOption.TIME_ASC -> list.sortedBy { it.readyInMinutes ?: Int.MAX_VALUE }
            SortOption.TIME_DESC -> list.sortedByDescending { it.readyInMinutes ?: Int.MIN_VALUE }
            SortOption.CAL_LOW_HIGH -> list.sortedBy { 0 }         // modelinde kalori yoksa dokunma
            SortOption.CAL_HIGH_LOW -> list.sortedByDescending { 0 }
        }

        adapter.submitList(list)

        // Tamamen boşsa placeholder gösterimi koru
        val hasAnyData = allCards.isNotEmpty()
        b.noRecipeLayout.isVisible = !hasAnyData
        b.rvFavorites.isVisible = hasAnyData
    }

    private fun matchesQuery(card: FavoriteRecipeCard): Boolean {
        val q = query.trim().lowercase(tr)
        if (q.isBlank()) return true
        val title = (card.title ?: "").lowercase(tr)
        val types = (card.dishTypes ?: emptyList()).joinToString(" ") { it.lowercase(tr) }
        return title.contains(q) || types.contains(q)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
