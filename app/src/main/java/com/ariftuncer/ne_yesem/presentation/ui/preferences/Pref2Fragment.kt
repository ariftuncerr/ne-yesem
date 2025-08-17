package com.ariftuncer.ne_yesem.presentation.ui.preferences

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentPref2Binding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.AllergenTag
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.Pref2Adapter

class Pref2Fragment : Fragment() {

    private var _binding: FragmentPref2Binding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { Pref2Adapter(::toggleAt) }

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
        _binding = FragmentPref2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        listeners()
        // RecyclerView + Flexbox (wrap'lı görünüm)
        val lm = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.CENTER
        }
        binding.recyclerTags.layoutManager = lm
        binding.recyclerTags.adapter = adapter
        binding.recyclerTags.setHasFixedSize(false)
        submit()


    }
    private fun listeners(){
        // Ekleme (IME Done)
        binding.etAddProduct.setOnEditorActionListener { tv, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { addFromInput(); true } else false
        }
        // Ekleme (Enter)
        binding.etAddProduct.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) { addFromInput(); true } else false
        }

        // ViewPager: Geç / Devam
        val pager = requireActivity().findViewById<ViewPager2>(R.id.prefViewPager)
        binding.pref2NextBtn.setOnClickListener { pager.currentItem = 2 }
        binding.pref2SkipText.setOnClickListener { pager.currentItem = 3 }

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
        adapter.submitList(items.toList()) // kopya ile diff tetikle
    }
}
