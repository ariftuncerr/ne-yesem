package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import PantrySelectAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.databinding.FragmentRecipeBinding
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.PantryViewModel
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.RecipesViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _b: FragmentRecipeBinding? = null
    private val b get() = _b!!

    private val pantryVm: PantryViewModel by viewModels()
    private val recipesVm: RecipesViewModel by viewModels()

    private lateinit var adapter: PantrySelectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentRecipeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPantryList()
        setupActions()

        // Buzdolabındaki tüm ürünleri çek ve listele
        pantryVm.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
        pantryVm.fetchAllItems()
    }

    private fun setupPantryList() = with(b.rvFridgeItems) {
        adapter = PantrySelectAdapter { _, _ -> /* seçim state'i adapter içinde tutuluyor */ }
            .also { this@RecipeFragment.adapter = it }
        setHasFixedSize(true)
        layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
    }

    private fun setupActions() = with(b) {
        addAllFridgeText.setOnClickListener {
            adapter.setAllSelected(adapter.currentList, true)
        }

        createRecipeBtn.setOnClickListener {
            val selectedNames = adapter.getSelected()
            if (selectedNames.isEmpty()) {
                Snackbar.make(root, "En az bir malzeme seçin", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Önerileri yükle ve ilk sonucu detay sayfasına gönder
            recipesVm.loadRecommendations(selectedNames, number = 1)
            recipesVm.recommended.observeOnce(viewLifecycleOwner) { list ->
                val first = list.firstOrNull()
                if (first == null) {
                    Snackbar.make(root, "Uygun tarif bulunamadı", Snackbar.LENGTH_SHORT).show()
                } else {
                    // --- NAVIGATION ---
                    // Safe Args kullanıyorsan (önerilir):
                    // val action = RecipeFragmentDirections
                    //     .actionRecipeFragmentToRecipeDetailFragment(first.id)
                    // findNavController().navigate(action)

                    // Safe Args yoksa Bundle ile:
                    val args = Bundle().apply { putInt("recipe_id", first.id) }
                    findNavController().navigate(
                        /* R.id.recipeDetailFragment */
                        // nav_graph'taki gerçek id’ni yaz:
                        com.ariftuncer.ne_yesem.R.id.recipeDetailFragment,
                        args
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}

/** Tek seferlik observe helper */
private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, obs: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            obs.onChanged(t)
        }
    })
}
