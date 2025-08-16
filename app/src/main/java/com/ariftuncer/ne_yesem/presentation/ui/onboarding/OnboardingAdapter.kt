package com.ariftuncer.ne_yesem.presentation.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter


class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstOnboardingFragment()
            1 -> SecondOnboardingFragment()
            2 -> ThirdOnboardingFragment()
            else -> throw IllegalArgumentException("Geçersiz pozisyon")
        }
    }

}

