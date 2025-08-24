package com.ariftuncer.ne_yesem.presentation.ui.home.homepages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentHomeBinding
import com.ariftuncer.ne_yesem.domain.model.DishType
import com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter.RecommendAdapter
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.PantryViewModel
import com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter.CategoryAdapter
import com.ariftuncer.ne_yesem.presentation.ui.home.homepages.adapter.PopularCategoryUi
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.RecipesViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val pantryVm: PantryViewModel by viewModels()
    private val recipesVm: RecipesViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var recommendAdapter: RecommendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpComponents()

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPopularCategoriesRv()
        submitPopularCategories()
        setUpListeners()
        setupRecommendRv()
        pantryVm.fetchAllItems()
        pantryVm.items.observe(viewLifecycleOwner) { list ->
            //val ingredients = list.mapNotNull { it.name?.trim() }.filter { it.isNotEmpty() }
            //if (ingredients.isNotEmpty()) recipesVm.loadRecommendations(ingredients, number = 10)
            val ingredients : List<String> = listOf("apples","flour","sugar")// For testing purposes
            recipesVm.loadRecommendations(ingredients, number = 10)
            println("Pantry items: $ingredients")

        }

        recipesVm.recommended.observe(viewLifecycleOwner) { items ->
            println(items)
            recommendAdapter.submitList(items)
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Merhaba"
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = false
        toolbar.subtitle = "Bugün ne pişireceksin?"
        toolbar.setSubtitleTextColor(ContextCompat.getColor(requireContext(), R.color.text900))
    }


    private fun setUpListeners() {
        binding.viewAllCtg.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_categoriesFragment)

        }
    }
    private fun setupRecommendRv() {
        recommendAdapter = RecommendAdapter { clickedId ->
            val args = Bundle().apply { putInt("recipeId", clickedId) }
            findNavController().navigate(
                R.id.action_nav_home_to_recipeDetailFragment,
                args
            )
        }
        binding.rvRecommends.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendAdapter
            setHasFixedSize(true)
        }
    }
    private fun setupPopularCategoriesRv() {
        categoryAdapter = CategoryAdapter { clicked ->
            // DishType.apiName ile CategoryItemFragment’a git
            val args = Bundle().apply { putString("categoryId", clicked.type.apiName) }
            findNavController().navigate(
                R.id.categoryItemFragment, // nav graph id'ni kullan
                args
            )
        }

        binding.rvPopularCtg.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun submitPopularCategories() {
        val items = listOf(
            PopularCategoryUi(
                DishType.BREAKFAST,
                DishType.BREAKFAST.displayNameTr,
                R.drawable.breakfast
            ),
            PopularCategoryUi(
                DishType.MAIN_COURSE,
                DishType.MAIN_COURSE.displayNameTr,
                R.drawable.launch
            ),
            PopularCategoryUi(DishType.DESSERT, DishType.DESSERT.displayNameTr, R.drawable.desert),
            PopularCategoryUi(DishType.SALAD, DishType.SALAD.displayNameTr, R.drawable.salad),
            PopularCategoryUi(DishType.SOUP, DishType.SOUP.displayNameTr, R.drawable.soup),
        )
        categoryAdapter.submitList(items)
    }

}