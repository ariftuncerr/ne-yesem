package com.ariftuncer.ne_yesem.presentation.ui.home.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentAccountBinding
import com.ariftuncer.ne_yesem.presentation.ui.profile.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    // Ad/soyad/telefon için mevcut VM
    private val vm: ProfileViewModel by viewModels()

    // Fotoğrafı iki fragment arasında paylaşmak için ortak VM (activity scope)
    private val sharedPhotoVm: SharedProfilePhotoViewModel by activityViewModels()

    // Medya seçici (yalnızca görsel)
    private val pickPhoto = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        // Account ekranda anında göster
        binding.settingsProfilePhoto.setImageURI(uri)
        // ProfileFragment'a yayınla
        sharedPhotoVm.setPhoto(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        vm.loadProfile()
        setUpComponents()
        listeners()
        observeViewModel()
        observeSharedPhoto() // daha önce seçilmiş foto varsa geri gelince de göster
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun setUpComponents() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.materialToolbar2)
        toolbar.title = "Hesap Bilgileri"
        // setTitleTextColor renk kaynağı değil doğrudan renk ister:
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.primary950))
        toolbar.isTitleCentered = true
        toolbar.subtitle = ""

        toolbar.apply {
            setNavigationIcon(R.drawable.back_24)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun observeViewModel() {
        vm.profile.observe(viewLifecycleOwner) { profile ->
            val fullName = profile?.name.orEmpty().trim().split(" ")
            val name = fullName.getOrNull(0).orEmpty()
            val surname = fullName.getOrNull(1).orEmpty()
            binding.nameEditTxt.setText(name)
            binding.surNameEditTxt.setText(surname)
            // Profil resmi sunucudan/yerelden geliyorsa burada set edebilirsin (opsiyonel)
            // Örn: profile.photoUriString?.let { binding.settingsProfilePhoto.load(it) }
        }
    }

    private fun listeners() {
        // Kaydet
        binding.settingsSaveBtn.setOnClickListener {
            val name = binding.nameEditTxt.text.toString().trim()
            val surname = binding.surNameEditTxt.text.toString().trim()
            val fullName = "$name $surname"
            val phoneNumber = binding.settingsTelText.text.toString().trim()
            vm.saveProfile(fullName, phoneNumber)
            findNavController().popBackStack()
        }

        // FAB -> fotoğraf seç
        binding.fabChangePhotoSettings.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun observeSharedPhoto() {
        // Ortak VM’de fotoğraf varsa, Account’a geri dönünce de gösterilsin
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedPhotoVm.photoUri.collect { uri ->
                    uri?.let { binding.settingsProfilePhoto.setImageURI(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
