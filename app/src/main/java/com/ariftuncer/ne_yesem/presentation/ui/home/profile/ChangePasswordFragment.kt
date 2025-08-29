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
import com.ariftuncer.ne_yesem.databinding.FragmentChangePasswordBinding
import com.ariftuncer.ne_yesem.presentation.ui.profile.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val vm: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        setUpListeners()
        setUpComponents()
        setUpObservers()

        return binding.root
    }
    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Şifre Değiştirme"
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
    private fun setUpListeners() {
        binding.passSaveBtn.setOnClickListener {
            val oldPassword = binding.passEditTxt.text.toString().trim()
            val newPassword = binding.newPassEditTxt.text.toString().trim()
            val confirmNewPassword = binding.approveEditTxt.text.toString().trim()

            if (newPassword == confirmNewPassword) {
                vm.changePassword(oldPassword, newPassword)

            } else {
                binding.approvePassLayout.error = "Şifreler eşleşmiyor"
            }
        }
    }
    private fun setUpObservers() {
        vm.passwordResult.observe(viewLifecycleOwner) {( msg, success) ->
            if (success) {
                binding.passEditTxt.text?.clear()
                binding.newPassEditTxt.text?.clear()
                binding.approveEditTxt.text?.clear()
                findNavController().popBackStack()
            } else {
                binding.passLayout.error =  "Şifre değiştirilemedi: $msg"
            }
        }
    }



}