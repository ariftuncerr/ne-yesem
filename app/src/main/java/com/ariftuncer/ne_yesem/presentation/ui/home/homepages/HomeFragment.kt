package com.ariftuncer.ne_yesem.presentation.ui.home.homepages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentHomeBinding
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpComponents()
        //setUpListeners()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Merhaba"
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.text950))
        toolbar.isTitleCentered = false
        toolbar.subtitle = "Bugün ne pişireceksin?"
        toolbar.setSubtitleTextColor(ContextCompat.getColor(requireContext(), R.color.text900))
    }
    private fun setUpListeners() {
        binding.viewAllCtg.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_categoriesFragment)
        }

    }

}