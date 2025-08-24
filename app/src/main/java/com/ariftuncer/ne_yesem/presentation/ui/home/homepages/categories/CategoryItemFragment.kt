package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.categories

import DishTypeAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentCategoryItemBinding
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryItemFragment : Fragment() {

    private var _b: FragmentCategoryItemBinding? = null
    private val b get() = _b!!

    private val vm: RecipesViewModel by viewModels()
    private lateinit var adapter: DishTypeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentCategoryItemBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DishTypeAdapter { recipe ->
            findNavController().navigate(R.id.recipeDetailFragment, Bundle().apply {
                putInt("recipeId", recipe.id)
            })
        }

        b.recipesByDishType.layoutManager= GridLayoutManager(requireContext(), 2)
        b.recipesByDishType.adapter = adapter

        val dishType = requireArguments().getString("categoryId") ?: "main course"
        vm.byDishType.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.loadByDishType(dishType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
