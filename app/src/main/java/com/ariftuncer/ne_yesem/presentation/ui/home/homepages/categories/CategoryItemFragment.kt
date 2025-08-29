package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.categories

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentCategoryItemBinding
import com.ariftuncer.ne_yesem.presentation.viewmodel.FavoritesViewModel
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.RecipesViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryItemFragment : Fragment() {

    private var _b: FragmentCategoryItemBinding? = null
    private val b get() = _b!!


    private val favVm: FavoritesViewModel by viewModels()
    private val vm: RecipesViewModel by viewModels()
    private lateinit var adapter: DishTypeAdapter
    private lateinit var ctgTitle : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentCategoryItemBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setUpComponents()
    }
    private fun setupAdapter() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null && favVm.ids.value == null) {
            favVm.refresh(uid) // ilk açılışta favori id'lerini çek
        }

        val dishTyp1 = requireArguments().getString("categoryId") ?: "main course" // <-- EKLENDİ
        adapter = DishTypeAdapter(
            favoriteIds = favVm.ids.value ?: emptySet(),
            onCardClick = { recipe -> // Detay
                findNavController().navigate(
                    R.id.recipeDetailFragment,
                    bundleOf("recipeId" to recipe.id)
                )
            },
            selectedDishTypeApiName = dishTyp1,                    // <-- EKLENDİ

            onHeartToggle = { recipeId, nowFav ->
                uid ?: return@DishTypeAdapter
                favVm.toggle(uid, recipeId, nowFavorite = nowFav)
            }
        )

        b.recipesByDishType.layoutManager = GridLayoutManager(requireContext(), 2)
        b.recipesByDishType.adapter = adapter

        // Favori id seti değişince kalpleri güncelle
        favVm.ids.observe(viewLifecycleOwner) { favIds ->
            adapter.updateFavoriteIds(favIds)
        }

        // Listeyi yükle & bağla
        val dishType = requireArguments().getString("categoryId") ?: "main course"
        ctgTitle = when(dishType) {
            "breakfast" -> "Kahvaltı"
            "main course" -> "Ana Yemek"
            "dessert" -> "Tatlı"
            "bread" -> "Hamur İşi"
            "salad" -> "Salata"
            "snack" -> "Atıştırmalık"
            "appetizer" -> "Meze"
            "beverage" -> "İçecek"
            "soup" -> "Çorba"
            else -> "Kategori"
        }
        vm.byDishType.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.loadByDishType(dishType)
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = ctgTitle
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_24)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
