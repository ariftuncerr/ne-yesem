package com.ariftuncer.ne_yesem.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.ActivityLoginRegisterBinding
import com.google.android.material.tabs.TabLayoutMediator

class ActivityLoginRegister : AppCompatActivity() {

    private lateinit var binding : ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter = LoginRegisterAdapter(this)
        binding.signViewPager.adapter = adapter

        TabLayoutMediator(binding.loginRegisterTabL, binding.signViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Giriş Yap"
                1 -> "Kayıt Ol"
                else -> ""
            }
        }.attach()

        // if onboarding is completed, set isFirstTime to false
        val prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("isFirstTime", false).apply()
    }
     fun replacePage(page : Int){
         when(page){
             0 -> binding.signViewPager.setCurrentItem(0)
             1 -> binding.signViewPager.setCurrentItem(1)
             else -> binding.signViewPager.setCurrentItem(0)
         }
    }
}