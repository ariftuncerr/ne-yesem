package com.ariftuncer.ne_yesem.presentation.ui.home.fridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentAddPantryBinding
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.adapter.AddPantryAdapter
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.catalog.SpoonacularCatalog
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.catalog.UnitMapper
import com.ariftuncer.ne_yesem.presentation.ui.home.fridge.dialog.UnitQtyDialog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.ariftuncer.ne_yesem.domain.model.IngredientCategory
import com.ariftuncer.ne_yesem.domain.model.PantryItem
import com.ariftuncer.ne_yesem.domain.model.UnitType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPantryFragment : Fragment() {

    private var _binding: FragmentAddPantryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PantryViewModel by viewModels()

    private val pantryItems = mutableListOf<PantryItem>()
    private val selectedItems = mutableSetOf<String>()
    private lateinit var adapter: AddPantryAdapter

    private var selectedCategory: IngredientCategory = IngredientCategory.VEGETABLES

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPantryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabs()
        setupRecycler()
        setupSearchDropdown()
        setupAddAllButton()
    }

    private fun setupToolbar() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Malzeme Ekle"
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
        toolbar.setNavigationIcon(R.drawable.back_24)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupTabs() {
        // Varsayılan 4 tab zaten layout’ta ise, sadece dinleyiciyi bağlamak yeterli
        binding.pantryCtg.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedCategory = tabToCategory(tab)
                binding.rvAddPantry.adapter?.notifyDataSetChanged()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.pantryCtg.getTabAt(0)?.select()
    }

    private fun setupRecycler() {
        adapter = AddPantryAdapter(
            items = pantryItems,
            selectedItems = selectedItems,
            onItemSelected = { item, isSelected ->
                if (isSelected) selectedItems.add(item.id) else selectedItems.remove(item.id)
            },
            onCardClick = { item ->
                val defaultLabel = when (item.unit) {
                    UnitType.KG -> "kg"
                    UnitType.GRAM -> "g"
                    UnitType.LITER -> "l"
                    UnitType.MILLILITER -> "ml"
                    UnitType.ADET -> "piece"
                }
                openUnitQtyDialog(defaultLabel, item.qty) { chosenUnitLabel, qty ->
                    val normalized = SpoonacularCatalog.normalizeUnit(chosenUnitLabel)
                    val domainUnit = UnitMapper.toDomainUnit(normalized)
                    val idx = pantryItems.indexOfFirst { it.id == item.id }
                    if (idx != -1) {
                        pantryItems[idx] = item.copy(unit = domainUnit, qty = qty)
                        selectedItems.add(item.id)
                        adapter.notifyItemChanged(idx)
                    }
                }
            }
        )
        binding.rvAddPantry.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAddPantry.adapter = adapter
    }

    private fun setupSearchDropdown() {
        val allSuggestions = SpoonacularCatalog.searchAll()
        val dd = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, allSuggestions)
        binding.addPantryEditTxt.setAdapter(dd)

        binding.addPantryEditTxt.setOnItemClickListener { parent, _, position, _ ->
            val picked = parent.getItemAtPosition(position) as String

            val realCategory = SpoonacularCatalog.resolveCategoryFor(picked, selectedCategory)
            ensureTabExistsAndSelect(realCategory)

            val suggestedUnitLabel = SpoonacularCatalog.suggestUnitFor(picked) ?: "piece"

            openUnitQtyDialog(defaultUnit = suggestedUnitLabel, defaultQty = 1) { chosenUnitLabel, qty ->
                val normalized = SpoonacularCatalog.normalizeUnit(chosenUnitLabel)
                val domainUnit = UnitMapper.toDomainUnit(normalized)

                val newItem = PantryItem(
                    id = System.currentTimeMillis().toString(),
                    name = picked,
                    category = realCategory,
                    qty = qty,
                    unit = domainUnit
                )
                pantryItems.add(0, newItem)
                selectedItems.add(newItem.id)
                adapter.notifyItemInserted(0)
                binding.rvAddPantry.scrollToPosition(0)
            }

            binding.addPantryEditTxt.text = null
        }
    }

    private fun openUnitQtyDialog(
        defaultUnit: String?,
        defaultQty: Int,
        onDone: (String, Int) -> Unit
    ) {
        UnitQtyDialog(
            unitOptions = SpoonacularCatalog.units,
            defaultUnit = defaultUnit,
            defaultQty = defaultQty,
            onDone = onDone
        ).show(parentFragmentManager, "unitQtyDialog")
    }

    private fun setupAddAllButton() {
        binding.addPantries.setOnClickListener {
            pantryItems.filter { it.id in selectedItems }.forEach { viewModel.addItem(it) }
            pantryItems.clear()
            selectedItems.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun tabToCategory(tab: TabLayout.Tab?): IngredientCategory = when (tab?.position) {
        0 -> IngredientCategory.VEGETABLES
        1 -> IngredientCategory.GRAINS
        2 -> IngredientCategory.MEAT_PROTEIN
        3 -> IngredientCategory.DAIRY
        else -> {
            val title = tab?.text?.toString().orEmpty()
            SpoonacularCatalog.categoryFromTabTitle(title) ?: IngredientCategory.VEGETABLES
        }
    }

    private fun ensureTabExistsAndSelect(category: IngredientCategory) {
        val title = SpoonacularCatalog.tabTitleFor(category)
        val existingIndex = (0 until binding.pantryCtg.tabCount).firstOrNull { i ->
            binding.pantryCtg.getTabAt(i)?.text == title
        }
        val toSelect = if (existingIndex != null) {
            binding.pantryCtg.getTabAt(existingIndex)
        } else {
            binding.pantryCtg.newTab().apply { text = title }.also { binding.pantryCtg.addTab(it) }
        }
        toSelect?.select()
        selectedCategory = category
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
