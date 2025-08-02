package com.ariftuncer.ne_yesem.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ariftuncer.ne_yesem.R

class ThirdOnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_third_onboarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startBtn = view.findViewById<Button>(R.id.startBtn)
        startBtn.setOnClickListener {
           // startActivity(Intent(requireContext(), LoginActivity::class.java))
           // requireActivity().finish()
        }



    }
}
