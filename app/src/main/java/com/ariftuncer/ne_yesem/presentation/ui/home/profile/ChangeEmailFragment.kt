package com.ariftuncer.ne_yesem.presentation.ui.home.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.google.android.material.appbar.MaterialToolbar


class ChangeEmailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_email, container, false)
        setUpComponents()
    }



    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Profil"
        toolbar.setTitleTextColor(R.color.primary950)
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""

        toolbar.apply {
            setNavigationIcon(R.drawable.back_24)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }




}