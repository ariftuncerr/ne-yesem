package com.ariftuncer.ne_yesem.presentation.ui.home.recipe

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ariftuncer.ne_yesem.presentation.ui.home.recipe.ingredients.IngredientsPageFragment

class IngredientsStepsPagerAdapter(
    fragment: Fragment,
    private val ingredients: ArrayList<String>,
    private val steps: ArrayList<String>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment =
        if (position == 0) IngredientsPageFragment.newInstance(ingredients)
        else StepsPageFragment.newInstance(steps)
}
