package com.ariftuncer.ne_yesem.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.ui.auth.ActivityLoginRegister
import com.ariftuncer.ne_yesem.ui.onboarding.OnboardingActivity

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