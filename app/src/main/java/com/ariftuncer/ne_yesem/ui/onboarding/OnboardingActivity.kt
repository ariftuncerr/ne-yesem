package com.ariftuncer.ne_yesem.ui.onboarding

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ActivityOnboardingBinding


class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.viewPager2.adapter = OnboardingAdapter(this)



    }


}
