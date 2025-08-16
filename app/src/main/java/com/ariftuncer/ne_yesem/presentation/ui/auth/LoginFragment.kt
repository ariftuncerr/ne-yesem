package com.ariftuncer.ne_yesem.presentation.login

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentLoginBinding
import com.ariftuncer.ne_yesem.di.AppGraph
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels { AppGraph.authVmFactory }

    // Google
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val idToken = account.idToken
                if (!idToken.isNullOrBlank()) {
                    setLoading(true)
                    // ViewModel tarafında isimler farklıysa (onGoogle), bu satırı ona göre değiştir:
                    viewModel.loginWithGoogle(idToken)
                } else {
                    showSnack("Google token alınamadı")
                }
            } catch (e: Exception) {
                showSnack("Google giriş hata/iptal: ${e.message}")
            }
        } else {
            showSnack("Google girişi iptal edildi")
        }
    }

    // Facebook
    private lateinit var fbCallbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogle()
        setupFacebook()
        setupObservers()
        setupListeners()
    }

    //region Setup

    private fun setupGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupFacebook() {
        fbCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : com.facebook.FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken?.token
                    if (!token.isNullOrBlank()) {
                        setLoading(true)
                        // ViewModel isimleri farklıysa (onFacebook) değiştir:
                        viewModel.loginWithFacebook(token)
                    } else {
                        showSnack("Facebook token alınamadı")
                    }
                }
                override fun onCancel() {
                    showSnack("Facebook girişi iptal edildi")
                }
                override fun onError(error: FacebookException) {
                    showSnack("Facebook hata: ${error.message}")
                }
            }
        )
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailEditTxt.text?.toString()?.trim().orEmpty()
            val password = binding.loginPasswordEditTxt.text?.toString()?.trim().orEmpty()

            if (email.isEmpty() || password.isEmpty()) {
                showSnack("Lütfen e-posta ve şifre girin")
                return@setOnClickListener
            }

            hideKeyboard(binding.root)
            setLoading(true)
            viewModel.login(email, password)
        }

        binding.forgotPasswordTxt.setOnClickListener {
            val email = binding.loginEmailEditTxt.text?.toString()?.trim().orEmpty()
            if (email.isEmpty()) {
                showSnack("Şifre sıfırlamak için e-posta girin")
                return@setOnClickListener
            }
            hideKeyboard(binding.root)
            setLoading(true)
            viewModel.sendResetEmail(email)
        }

        binding.loginGoogleBtn.setOnClickListener {
            startGoogleSignIn()
        }

        binding.loginfacebookBtn.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
        }
    }

    private fun setupObservers() {
        viewModel.login.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            if (success) {
                showSnack(message)
                // TODO: başarılı girişte yönlendirme
            } else {
                showSnack("Giriş başarısız: $message")
            }
        }

        // Pair sırası: (success, message)
        viewModel.reset.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            if (success) showSnack(message)
            else showSnack("Şifre sıfırlama başarısız: $message")
        }

        viewModel.google.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            showSnack(message)
            // success true ise yönlendirme
        }

        viewModel.facebook.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            showSnack(message)
            // success true ise yönlendirme
        }
    }

    //endregion

    //region Actions

    private fun startGoogleSignIn() {
        val intent = googleSignInClient.signInIntent
        googleLauncher.launch(intent)
    }

    @Deprecated("Facebook SDK için onActivityResult yönlendirmesi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::fbCallbackManager.isInitialized) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    //endregion

    //region UI helpers

    private fun showSnack(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setLoading(loading: Boolean) {
        // Dim/Buğulu overlay
        binding.loadingOverlaylogin?.isVisible = loading

        // (İsteğe bağlı) Android 12+ gerçek blur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (loading) {
                binding.contentRoot?.setRenderEffect(
                    RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                )
            } else {
                binding.contentRoot?.setRenderEffect(null)
            }
        }

        // Formu kilitle
        binding.loginBtn.isEnabled = !loading
        binding.loginGoogleBtn.isEnabled = !loading
        binding.loginfacebookBtn.isEnabled = !loading
        binding.loginEmailEditTxt.isEnabled = !loading
        binding.loginPasswordEditTxt.isEnabled = !loading
        binding.progressBar?.isVisible = loading
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //endregion

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
