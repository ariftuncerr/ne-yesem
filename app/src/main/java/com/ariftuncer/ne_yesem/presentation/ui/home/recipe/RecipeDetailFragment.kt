package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentRecipeDetailBinding
import com.ariftuncer.ne_yesem.presentation.viewmodel.recipe.RecipeDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailBinding
    private val vm: RecipeDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        setUpComponents()
        setUpDetails()
        return binding.root

    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val bottomBar = requireActivity().findViewById<View>(R.id.bottomNavigationView)
        val recipeBtn = requireActivity().findViewById<View>(R.id.fab_recipes)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = ""
        toolbar.isTitleCentered = false
        toolbar.subtitle = ""
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_24)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack()
            bottomBar.visibility = View.GONE
            recipeBtn.visibility = View.GONE
            toolbar.navigationIcon = null

        }
        bottomBar.visibility = View.GONE
        recipeBtn.visibility = View.GONE

    }

    private fun setUpDetails() {
        val id = requireArguments().getInt("recipeId")
        vm.load(id)
        println("Recipe ID: $id")

        vm.detail.observe(viewLifecycleOwner) { d ->

            println("Recipe detail: $d")
            if (d == null) { /* error state göster */ return@observe }
            /*binding.preaparingView.visibility = View.GONE

            (binding.root as ViewGroup).findViewById<NestedScrollView>(R.id.nestedScrollView)?.visibility = View.VISIBLE*/
            Glide.with(this).load(d.image).into(binding.recipeImg)
            binding.recipeTt.text = d.title
            binding.recipeDetailTxt.text = d.summaryPlain
            binding.timeTxt.text = d.readyInMinutes?.let { "$it dk" } ?: "-"
            binding.personsTxt.text = d.servings?.let { "$it kişilik" } ?: "-"
            binding.coloriesTxt.text = d.caloriesText ?: "-"

            val ingredients = ArrayList(d.ingredients)
            val steps = ArrayList(d.steps)

            binding.ingOrPreparationViewPager.adapter =
                IngredientsStepsPagerAdapter(this, ingredients, steps)
            binding.ingOrPreparationViewPager.offscreenPageLimit = 2

            com.google.android.material.tabs.TabLayoutMediator(
                binding.ingOrPreparationTab,
                binding.ingOrPreparationViewPager
            ) { tab, pos ->
                tab.text = if (pos == 0) "Malzemeler" else "Yapılışı"
            }.attach()
        }

    }
}