package com.ariftuncer.ne_yesem.presentation.ui.preferences

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ActivityPreferencesBinding
import com.ariftuncer.ne_yesem.databinding.FragmentFirstOnboardingBinding
import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.ariftuncer.ne_yesem.presentation.preferences.PreferencesViewModel
import com.ariftuncer.ne_yesem.presentation.ui.preferences.prefAdapters.PreferencesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferencesBinding
    private val vm: PreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vm.load()
        setUpViewPager()
        listeners()

    }
    private fun setUpViewPager() {
        binding.prefViewPager.adapter = PreferencesAdapter(this)

    }
    private fun listeners() {
        binding.prefViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val step = position + 1
                    val totalSteps = binding.prefViewPager.adapter?.itemCount ?: 1

                    // Step bar progress
                    val progress = (step * 100) / totalSteps
                    binding.stepBar.setProgressCompat(progress, true)

                    // Text güncelleme
                    binding.stepText.text = "$step/$totalSteps"
                }
            }
        )

        binding.materialToolbar.setNavigationOnClickListener {
            val current = binding.prefViewPager.currentItem
            if (current > 0) {
                binding.prefViewPager.currentItem = current - 1
            }
        }
    }

}