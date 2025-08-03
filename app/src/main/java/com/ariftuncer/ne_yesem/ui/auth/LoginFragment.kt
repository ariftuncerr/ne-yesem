package com.ariftuncer.ne_yesem.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ariftuncer.ne_yesem.databinding.FragmentLoginBinding
import com.ariftuncer.ne_yesem.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.viewModels
import com.ariftuncer.ne_yesem.R

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupGoogleSignIn()
        controlClickableComponents()
        observeViewModel()

        return binding.root
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id)) // veya web_client_id
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun controlClickableComponents() {
        // Register sayfasına geç
        binding.clickForRegisterText.setOnClickListener {
            (requireActivity() as? ActivityLoginRegister)?.replacePage(1)
        }

        // E-posta/şifre login
        binding.loginBtn.setOnClickListener {
            if (validateInputs()) {
                authViewModel.login(
                    binding.loginEmailEditTxt.text.toString().trim(),
                    binding.loginPasswordEditTxt.text.toString().trim()
                )
            }
        }

        // Google login
        binding.loginGoogleBtn.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                val intent = googleSignInClient.signInIntent
                startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.loginResult.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                Snackbar.make(binding.root, message ?: "Giriş başarılı", Snackbar.LENGTH_SHORT).show()
                // Ana ekrana yönlendirme yapılabilir
            } else {
                Snackbar.make(binding.root, message ?: "Giriş başarısız", Snackbar.LENGTH_SHORT).show()
            }
        }

        authViewModel.loginWithGoogleResult.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                Snackbar.make(binding.root, message ?: "Google ile giriş başarılı", Snackbar.LENGTH_SHORT).show()
                // Ana ekrana yönlendirme yapılabilir
            } else {
                Snackbar.make(binding.root, message ?: "Google ile giriş başarısız", Snackbar.LENGTH_SHORT).show()
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
                    authViewModel.loginWithGoogle(idToken)
                } else {
                    Snackbar.make(binding.root, "ID token alınamadı", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Snackbar.make(binding.root, "Google giriş hatası: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.loginEmailEditTxt.text.toString().trim()
        val password = binding.loginPasswordEditTxt.text.toString().trim()

        var isValid = true

        if (email.isEmpty()) {
            binding.loginEmailInputLayout.error = "E-posta boş olamaz"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.loginEmailInputLayout.error = "Geçersiz e-posta"
            isValid = false
        } else {
            binding.loginEmailInputLayout.error = null
        }

        if (password.length < 6) {
            binding.loginPasswordLayout.error = "En az 6 karakter"
            isValid = false
        } else {
            binding.loginPasswordLayout.error = null
        }

        return isValid
    }
}
