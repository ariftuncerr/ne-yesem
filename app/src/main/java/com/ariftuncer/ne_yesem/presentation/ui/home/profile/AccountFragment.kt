package com.ariftuncer.ne_yesem.presentation.ui.home.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentAccountBinding
import com.ariftuncer.ne_yesem.presentation.ui.profile.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val vm : ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        vm.loadProfile()
        setUpComponents()
        listeners()
        observeViewModel()
        return binding.root
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Hesap Bilgileri"
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
    private fun observeViewModel() {
        // Load user data
        vm.profile.observe (viewLifecycleOwner) { profile ->
            val fullName = profile?.name.orEmpty().trim().split(" ")
            val name = fullName.getOrNull(0).orEmpty()
            val surname = fullName.getOrNull(1).orEmpty()
            println("Name: $fullName")
            binding.nameEditTxt.setText(name)
            binding.surNameEditTxt.setText(surname)
        }

    }
    private fun listeners(){
        binding.settingsSaveBtn.setOnClickListener {
            val name = binding.nameEditTxt.text.toString().trim()
            val surname = binding.surNameEditTxt.text.toString().trim()
            val fullName = "$name $surname"
            val phoneNumber = binding.settingsTelText.text.toString().trim()
            vm.saveProfile(fullName, phoneNumber)
            findNavController().popBackStack()
        }
    }
}