package com.ariftuncer.ne_yesem.presentation.ui.home.fridge

import PantryAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentFridgeBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.ariftuncer.ne_yesem.domain.model.IngredientCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FridgeFragment : Fragment() {

    private var _binding: FragmentFridgeBinding? = null
    private val binding get() = _binding!!

    private lateinit var pantryAdapter: PantryAdapter
    private val viewModel: PantryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFridgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //deneme
        viewModel.fetchAllItems()
        // Set up components, RecyclerView, and tabs
        setUpComponents()
        setupRecycler()
        setupTabs()
        observePantry()
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        // Toolbar
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Dolabım"
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
        toolbar.navigationIcon = null
        // Ekle butonu
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_nav_fridge_to_addPantryFragment)
        }
    }

    private fun setupRecycler() {
        pantryAdapter = PantryAdapter(
            onPlus = { item ->
                viewModel.updateItem(item.copy(qty = item.qty + 1))
            },
            onMinus = { item ->
                if (item.qty > 1) viewModel.updateItem(item.copy(qty = item.qty - 1))
            },
            onDelete = { item ->
                viewModel.deleteItem(item.category, item.id)
            }
        )
        binding.rvPantry.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPantry.adapter = pantryAdapter
    }

    private fun setupTabs() {
        binding.pantryCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val cat = when (tab?.position) {
                    0 -> IngredientCategory.VEGETABLES
                    1 -> IngredientCategory.GRAINS
                    2 -> IngredientCategory.MEAT_PROTEIN
                    3 -> IngredientCategory.DAIRY
                    else -> IngredientCategory.VEGETABLES
                }
                viewModel.fetchItemsByCategory(cat)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.pantryCategories.getTabAt(0)?.select()
    }

    private fun observePantry() {
        viewModel.items.observe (viewLifecycleOwner){ items ->
            println("Pantry items: $items")

        }
        viewModel.categoryItems.observe(viewLifecycleOwner) { items ->
            println("Category items: $items")
            pantryAdapter.submitList(items)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
