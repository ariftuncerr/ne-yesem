package com.ariftuncer.ne_yesem.presentation.ui.preferences

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PreferencesAdapter (activity : FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> Pref1Fragment()
            1 -> Pref2Fragment()
            2 -> Pref3Fragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}