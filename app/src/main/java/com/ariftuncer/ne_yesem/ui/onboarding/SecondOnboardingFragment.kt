package com.ariftuncer.ne_yesem.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentFirstOnboardingBinding
import com.ariftuncer.ne_yesem.databinding.FragmentSecondOnboardingBinding

class SecondOnboardingFragment : Fragment() {
    private lateinit var binding: FragmentSecondOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondOnboardingBinding.inflate(layoutInflater)
        val view = binding.root

        binding.onSecondForwardBtn.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.viewPager2).currentItem = 2
        }

        binding.onSecondSkipTxt.setOnClickListener {
            requireActivity().findViewById<ViewPager2>(R.id.viewPager2).currentItem = 2
        }

        return view
    }

}