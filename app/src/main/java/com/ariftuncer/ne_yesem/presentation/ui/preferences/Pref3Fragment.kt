package com.ariftuncer.ne_yesem.presentation.ui.preferences

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentPref3Binding
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.AllergenTag
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.Pref3Adapter

class Pref3Fragment : Fragment() {

    private var _binding: FragmentPref3Binding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { Pref3Adapter(::toggleAt) }

    // UI-only state
    private val items = mutableListOf(
        AllergenTag("Domates"),
        AllergenTag("Peynir"),
        AllergenTag("Yumurta"),
        AllergenTag("Fıstık"),
        AllergenTag("Kakao"),
        AllergenTag("Biber")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPref3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
        val lm = com.google.android.flexbox.FlexboxLayoutManager(requireContext()).apply {
            flexDirection = com.google.android.flexbox.FlexDirection.ROW
            flexWrap = com.google.android.flexbox.FlexWrap.WRAP
            justifyContent = com.google.android.flexbox.JustifyContent.FLEX_START
            alignItems = com.google.android.flexbox.AlignItems.CENTER
        }
        binding.recyclerTags2.layoutManager = lm
        binding.recyclerTags2.adapter = adapter
        binding.recyclerTags2.setHasFixedSize(false)
        submit()
    }
    private fun listeners() {
        // Ekleme (IME Done)
        binding.etAddProduct.setOnEditorActionListener { tv, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { addFromInput(); true } else false
        }
        // Ekleme (Enter)
        binding.etAddProduct.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) { addFromInput(); true } else false
        }

    }

    private fun toggleAt(position: Int) {
        if (position in items.indices) {
            val it = items[position]
            items[position] = it.copy(selected = !it.selected)
            submit()
        }
    }

    private fun addFromInput() {
        val text = binding.etAddProduct.text?.toString()?.trim().orEmpty()
        val exists = items.any { it.label.equals(text, ignoreCase = true) }
        if (text.length in 2..30 && !exists) {
            items += AllergenTag(text, selected = true) // yeni ekleneni seçili getir
            binding.etAddProduct.text = null
            submit()
        } else {
            binding.etAddProduct.error = "Geçerli bir ürün girin"
        }
    }

    private fun submit() {
        adapter.submitList(items.toList())
    }

}