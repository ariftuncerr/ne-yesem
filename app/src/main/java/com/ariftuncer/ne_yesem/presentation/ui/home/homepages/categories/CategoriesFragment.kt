package com.ariftuncer.ne_yesem.presentation.ui.home.homepages.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ariftuncer.ne_yesem.R
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.databinding.FragmentCatagoriesBinding
import com.ariftuncer.ne_yesem.domain.model.DishType
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriesFragment : Fragment() {
    private var _b: FragmentCatagoriesBinding? = null
    private val b get() = _b!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentCatagoriesBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComponents()
        // Kartları DishType ile eşle
        bindCard(b.card1, DishType.BREAKFAST)     // Kahvaltı
        bindCard(b.card2, DishType.MAIN_COURSE)   // Ana Yemek
        bindCard(b.card3, DishType.DESSERT)       // Tatlı
        bindCard(b.card4, DishType.BREAD)         // Hamurişi -> Bread
        bindCard(b.card5, DishType.SALAD)         // Salata
        bindCard(b.card6, DishType.SNACK)         // Atıştırmalık
        bindCard(b.card7, DishType.APPETIZER)     // Meze
        bindCard(b.card8, DishType.BEVERAGE)      // İçecek
        bindCard(b.card9, DishType.SOUP)          // Çorba
    }

    private fun bindCard(view: View, type: DishType) {
        view.setOnClickListener {
            val args = bundleOf("categoryId" to type.apiName)
            findNavController().navigate(R.id.categoryItemFragment, args)
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Kategoriler"
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
        toolbar.setSubtitleTextColor(ContextCompat.getColor(requireContext(), R.color.text900))
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_24)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    }


    override fun onDestroyView() {

        super.onDestroyView()
        _b = null
    }
}
