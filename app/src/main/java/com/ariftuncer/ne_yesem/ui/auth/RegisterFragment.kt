package com.ariftuncer.ne_yesem.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ariftuncer.ne_yesem.databinding.FragmentRegisterBinding
import com.ariftuncer.ne_yesem.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Google giriş yapılandırması
        setupGoogleSignIn()

        // Tıklanabilir bileşenleri tanımla
        controlClickableComponents()

        // ViewModel gözlemleme
        observeViewModel()

        return binding.root
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.ariftuncer.ne_yesem.R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun controlClickableComponents() {
        // E-posta ile kayıt
        binding.registerBtn.setOnClickListener {
            if (validateInputs()) {
                val email = binding.registerEmailEditTxt.text.toString().trim()
                val password = binding.registerPasswordEditTxt.text.toString().trim()
                authViewModel.register(email, password)
            }
        }

        // Google ile kayıt/giriş
        binding.registerGoogleBtn.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                val intent = googleSignInClient.signInIntent
                startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
            }
        }

        // Zaten hesabım var (Giriş sayfasına geç)
        binding.haveAccountLoginText.setOnClickListener {
            (requireActivity() as? ActivityLoginRegister)?.replacePage(0)
        }
    }

    private fun observeViewModel() {
        // Email/şifre kayıt sonucu
        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(requireContext(), message ?: "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                // Ana sayfaya yönlendirme yapılabilir
            } else {
                Snackbar.make(binding.root, message ?: "Kayıt başarısız", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Google login/kayıt sonucu
        authViewModel.googleRegisterResult.observe(viewLifecycleOwner) { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(requireContext(), message ?: "Google ile Kayıt başarılı", Toast.LENGTH_SHORT).show()
                // Ana sayfaya yönlendirme yapılabilir
            } else {
                Snackbar.make(binding.root, message ?: "Google ile Kayıt başarısız", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    authViewModel.registerWithGoogle(idToken)
                } else {
                    Snackbar.make(binding.root, "ID token alınamadı", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Snackbar.make(binding.root, "Google giriş hatası: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.registerEmailEditTxt.text.toString().trim()
        val password = binding.registerPasswordEditTxt.text.toString().trim()
        val confirmPassword = binding.registerPasswordApproveEditTxt.text.toString().trim()

        var isValid = true

        if (email.isEmpty()) {
            binding.registerEmailLayout.error = "E-posta boş olamaz"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.registerEmailLayout.error = "Geçersiz e-posta adresi"
            isValid = false
        } else {
            binding.registerEmailLayout.error = null
        }

        if (password.length < 6) {
            binding.registerPasswordLayout.error = "Şifre en az 6 karakter olmalı"
            isValid = false
        } else {
            binding.registerPasswordLayout.error = null
        }

        if (confirmPassword != password) {
            binding.registerPasswordApproveLayout.error = "Şifreler uyuşmuyor"
            isValid = false
        } else {
            binding.registerPasswordApproveLayout.error = null
        }

        return isValid
    }
}
