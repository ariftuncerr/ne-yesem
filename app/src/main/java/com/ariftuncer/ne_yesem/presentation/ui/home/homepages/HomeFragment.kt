package com.ariftuncer.ne_yesem.presentation.ui.home.homepages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.data.repository.RecipeRepositoryImpl
import com.ariftuncer.ne_yesem.databinding.FragmentHomeBinding
import com.ariftuncer.ne_yesem.presentation.recipe.RecommendAdapter
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.PantryViewModel
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.RecipesViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val pantryVm: PantryViewModel by viewModels()
    private val recipesVm: RecipesViewModel by viewModels()

    private lateinit var recommendAdapter: RecommendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpComponents()
        //setUpListeners()

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        /*binding.viewAllCtg.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_categoriesFragment)
        }*/

    }
    private fun setupRecommendRv() {
        recommendAdapter = RecommendAdapter { clickedId ->
            // TODO: Detay sayfasına geçişi sonra ekleyeceğiz
        }
        binding.rvRecommends.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendAdapter
            setHasFixedSize(true)
        }
    }

}