package com.ariftuncer.ne_yesem.presentation.ui.home.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentProfileBinding
import com.ariftuncer.ne_yesem.presentation.preferences.PreferencesViewModel
import com.ariftuncer.ne_yesem.presentation.ui.auth.ActivityLoginRegister
import com.ariftuncer.ne_yesem.presentation.ui.profile.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.ne_yesem.domain.model.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val vm: ProfileViewModel by viewModels()
    private val prefViewModel: PreferencesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        loadUserData()
        val view = binding.root
        setUpComponents()
        setupRows()
        setUpObservers()
        return view
    }
    private fun loadUserData() {
        vm.loadProfile()
        prefViewModel.getPreferences()
        vm.profile.observe (viewLifecycleOwner){ profile ->
            println("profile: $profile")
            binding.profileNameTxt.text = profile?.name.orEmpty()
            binding.profileEmailTxt.text = profile?.email.orEmpty()
        }
        prefViewModel.preferences.observe (viewLifecycleOwner) { preferences ->
            val diet = preferences?.diet ?: "Belirtilmemiş"
            val allergens = preferences?.allergens?.joinToString(", ") ?: "Belirtilmemiş"
            val unlikedFoods = preferences?.unlikedFoods?.joinToString(", ") ?: "Belirtilmemiş"
            binding.tvDiet.text = "Beslenme Tarzı: $diet"
            binding.tvAllergy.text = "Alerjenler: $allergens"
            binding.tvDislikes.text = "Sevmediğim Ürünler: $unlikedFoods"
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Profil"
        toolbar.setTitleTextColor(R.color.primary950)
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""

        //sign Out
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                vm.signOut()

            }
        }
        toolbar.navigationIcon = null
    }
    private fun setUpObservers() {
        vm.signOutResult.observe (viewLifecycleOwner){ success ->
            if (success) {
                val intent = Intent(requireActivity(), ActivityLoginRegister::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Snackbar.make(binding.root,"Çıkış yapılamadı", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRows() {
        val rowShopping = binding.rowShopping
        rowShopping.tvTitle.text = "Alışveriş Listelerim"
        rowShopping.ivIcon.setImageResource(R.drawable.shopping_cart)
        rowShopping.ivChevron.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_shoppingListFragment)
        }

        val settings = binding.rowSettings
        settings.tvTitle.text = "Ayarlar"
        settings.ivIcon.setImageResource(R.drawable.settings)
        settings.ivChevron.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_accountFragment)

        }
        val changeEmail = binding.rowChangeEmail
        changeEmail.tvTitle.text = "E-posta Değiştirme"
        changeEmail.ivIcon.setImageResource(R.drawable.change_email)
        changeEmail.ivChevron.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_changeEmailFragment)
        }
        val changePass = binding.rowChangePassword
        changePass.tvTitle.text = "Şifre Değiştirme"
        changePass.ivIcon.setImageResource(R.drawable.change_pass)
        changePass.ivChevron.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_changePasswordFragment)
        }
        val changeLanguage = binding.rowLanguage
        changeLanguage.tvTitle.text = "Dil Seçenekleri"
        changeLanguage.ivIcon.setImageResource(R.drawable.change_launguage)
        changeLanguage.ivChevron.setOnClickListener {
        }

        binding.profileEmailTxt.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_accountFragment)
        }
        binding.profileNameTxt.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_accountFragment)
        }

    }
}