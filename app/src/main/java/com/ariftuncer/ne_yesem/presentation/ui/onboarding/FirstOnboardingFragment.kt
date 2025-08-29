package com.ariftuncer.ne_yesem.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentFirstOnboardingBinding


class FirstOnboardingFragment : Fragment() {
    private lateinit var binding: FragmentFirstOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstOnboardingBinding.inflate(layoutInflater)
        val view = binding.root

        binding.OnFirstForwardBtn.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.viewPager2).currentItem = 1
        }

        binding.onFirstSkipTxt.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.viewPager2).currentItem = 2
        }

        return view
    }
}