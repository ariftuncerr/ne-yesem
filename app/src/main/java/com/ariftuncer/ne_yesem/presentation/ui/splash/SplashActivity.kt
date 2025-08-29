package com.ariftuncer.ne_yesem.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ariftuncer.ne_yesem.presentation.ui.auth.ActivityLoginRegister
import com.ariftuncer.ne_yesem.presentation.ui.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean("isFirstTime", true)

        val intent = if (isFirstTime) {
            Intent(this, OnboardingActivity::class.java)
        } else {
            Intent(this, ActivityLoginRegister::class.java)
        }

        startActivity(intent)
        finish()
    }
}