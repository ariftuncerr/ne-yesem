package com.ariftuncer.ne_yesem.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.presentation.ui.auth.ActivityLoginRegister

class ThirdOnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_third_onboarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startBtn = view.findViewById<Button>(R.id.startBtn)
        startBtn.setOnClickListener {
           startActivity(Intent(requireContext(), ActivityLoginRegister::class.java))
            requireActivity().finish()
        }
    }
}
