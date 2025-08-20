package com.ariftuncer.ne_yesem.presentation.ui.home.fridge

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentAddPantryBinding
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.adapter.AddPantryAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.ne_yesem.domain.model.IngredientCategory
import com.ne_yesem.domain.model.PantryItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPantryFragment : Fragment() {
    private lateinit var binding: FragmentAddPantryBinding
    private val viewModel: PantryViewModel by viewModels()
    private val pantryItems = mutableListOf<PantryItem>()
    private lateinit var adapter: AddPantryAdapter
    private val selectedItems = mutableSetOf<String>()
    private var selectedCategory: IngredientCategory = IngredientCategory.VEGETABLES

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddPantryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComponents()
        setupTabs()
        setupRecycler()
        setupAddListener()

        binding.addPantries.setOnClickListener {
            val selected = pantryItems.filter { selectedItems.contains(it.id) }
            selected.forEach { viewModel.addItem(it) }
            pantryItems.clear()
            selectedItems.clear()
            adapter.notifyDataSetChanged()
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        // Toolbar
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Yeni Malzeme Ekle"
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""

        toolbar.apply {
            setNavigationIcon(R.drawable.back_24)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupRecycler() {
        adapter = AddPantryAdapter(
            items = pantryItems,
            selectedItems = selectedItems,
            onItemSelected = { item, isSelected ->
                if (isSelected) selectedItems.add(item.id) else selectedItems.remove(item.id)
                adapter.notifyDataSetChanged()
            }
        )
        binding.rvAddPantry.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAddPantry.adapter = adapter
    }

    private fun setupAddListener() {
        binding.addPantryEditTxt.setOnEditorActionListener { v, actionId, event ->
            val text = binding.addPantryEditTxt.text?.toString()?.trim()
            if (!text.isNullOrEmpty()) {
                val newItem = PantryItem(
                    id = System.currentTimeMillis().toString(),
                    name = text,
                    category = selectedCategory,
                    qty = 1,
                    unit = com.ne_yesem.domain.model.UnitType.ADET
                )
                pantryItems.add(newItem)
                selectedItems.add(newItem.id)
                adapter.notifyDataSetChanged()
                binding.addPantryEditTxt.text?.clear()
                true
            } else false
        }
    }

    private fun setupTabs() {
        binding.pantryCtg.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                selectedCategory = when (tab?.position) {
                    0 -> IngredientCategory.VEGETABLES
                    1 -> IngredientCategory.GRAINS
                    2 -> IngredientCategory.MEAT_PROTEIN
                    3 -> IngredientCategory.DAIRY
                    else -> IngredientCategory.VEGETABLES
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
        binding.pantryCtg.getTabAt(0)?.select()
    }


}