package com.ariftuncer.ne_yesem.presentation.ui.preferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentPref1Binding
import com.ariftuncer.ne_yesem.presentation.preferences.PreferencesViewModel
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.Pref1Adapter
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.Pref1Card
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Pref1Fragment : Fragment() {
    private lateinit var binding : FragmentPref1Binding
    private val vm: PreferencesViewModel by activityViewModels()

    private val items = listOf(
        Pref1Card(R.drawable.keto, "Klasik"),
        Pref1Card(R.drawable.keto, "Şekersiz"),
        Pref1Card(R.drawable.keto, "Vejetaryen"),
        Pref1Card(R.drawable.keto, "Vegan"),
        Pref1Card(R.drawable.keto, "Ketojenik"),
    )
    private var selectedIndex: Int = -1
    private val adapter by lazy { Pref1Adapter(items, ::onCardClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPref1Binding.inflate(inflater, container, false)
        val view : View = binding.root
        listeners()
        binding.pref1RecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.pref1RecyclerView.adapter = adapter
        return view
    }
    private fun listeners() {
        binding.pref1NextBtn.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.prefViewPager).currentItem = 1
        }
        binding.pref1SkipText.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.prefViewPager).currentItem = 2
        }
    }
    private fun onCardClick(position: Int) {
        if (selectedIndex != position) {
            selectedIndex = position
            vm.selectDiet(items[position].text)
            adapter.setSelected(position)
        }
    }
}