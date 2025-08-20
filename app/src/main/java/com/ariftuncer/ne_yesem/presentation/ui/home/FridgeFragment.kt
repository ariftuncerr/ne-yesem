package com.ariftuncer.ne_yesem.presentation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ariftuncer.ne_yesem.R
import com.google.android.material.appbar.MaterialToolbar


class FridgeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpComponents()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fridge, container, false)
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Dolabım"
        toolbar.setTitleTextColor(R.color.text950)
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""
    }


}