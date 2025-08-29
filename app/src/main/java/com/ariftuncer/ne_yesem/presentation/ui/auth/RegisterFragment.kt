package com.ariftuncer.ne_yesem.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ariftuncer.ne_yesem.R
import com.ariftuncer.ne_yesem.databinding.FragmentRegisterBinding
import com.ariftuncer.ne_yesem.di.AppGraph
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.AuthViewModel
import com.ariftuncer.ne_yesem.presentation.ui.home.HomeActivity
import com.ariftuncer.ne_yesem.presentation.ui.preferences.PreferencesActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    // ViewBinding (leak önlemek için nullable pattern)
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var overlay: View
    private lateinit var root: View
    private lateinit var spinner: View

    private val viewModel: AuthViewModel by viewModels { AppGraph.authVmFactory }

    // Google
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (!idToken.isNullOrBlank()) {
                    setLoading(true)
                    viewModel.loginWithGoogle(idToken)
                } else {
                    showSnack("ID token alınamadı")
                }
            } catch (e: ApiException) {
                showSnack("Google giriş hatası: ${e.message}")
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogle()
        setupFacebook()
        setupListeners()
        setupObservers()
    }

    //region Setup

    private fun setupGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupFacebook() {
        fbCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken?.token
                    if (!token.isNullOrBlank()) {
                        setLoading(true)
                        viewModel.loginWithFacebook(token)
                    } else {
                        showSnack("Facebook token alınamadı")
                    }
                }
                override fun onCancel() {
                    showSnack("Facebook ile giriş iptal edildi")
                }
                override fun onError(error: FacebookException) {
                    showSnack("Facebook ile giriş hatası: ${error.message}")
                }
            }
        )
    }

    //endregion

    //region UI events

    private fun setupListeners() {
        // E-posta ile kayıt
        binding.registerBtn.setOnClickListener {
            if (validateInputs()) {
                val email = binding.registerEmailEditTxt.text?.toString()?.trim().orEmpty()
                val password = binding.registerPasswordEditTxt.text?.toString()?.trim().orEmpty()
                hideKeyboard(binding.root)
                setLoading(true)
                // ViewModel’inde register(email, password) beklediğini görüyorum
                viewModel.register(email, password)
            }
        }

        // Google ile giriş/kayıt
        binding.registerGoogleBtn.setOnClickListener {
            // Her zaman hesap seçimi için signOut çağrısı
            googleSignInClient.signOut().addOnCompleteListener {
                googleLauncher.launch(googleSignInClient.signInIntent)
            }
        }

        // Facebook ile giriş/kayıt
        binding.registerFacebookBtn.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        // Zaten hesabım var → Login sayfasına geç
        binding.haveAccountLoginText.setOnClickListener {
            // Eğer ActivityLoginRegister içinde sayfa değiştiriyorsan:
            (requireActivity() as? ActivityLoginRegister)?.replacePage(0)
        }
    }

    private fun setupObservers() {
        // Email/Şifre ile kayıt sonucu
        viewModel.register.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            if (success) {
                showSnack(message)
                val isNewUser = viewModel.isNewUserRegister // ViewModel'da bu property olmalı
                if (isNewUser == true) {
                    startActivity(Intent(requireContext(), PreferencesActivity::class.java))
                    requireActivity().finish()
                } else {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                }
            } else {
                showSnack(message.ifBlank { "Kayıt başarısız" })
            }
        }

        // Google sonucu
        viewModel.google.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            showSnack(message.ifBlank { if (success) "Google ile giriş başarılı" else "Google ile giriş başarısız" })
            if (success) {
                // isNewUser kontrolü için ViewModel'dan AuthOutcome alınmalı
                // Örnek: viewModel.authOutcome.value?.isNewUser
                val isNewUser = viewModel.isNewUserGoogle // ViewModel'da bu property olmalı
                if (isNewUser == true) {
                    startActivity(Intent(requireContext(), PreferencesActivity::class.java))
                    requireActivity().finish()
                } else {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        // Facebook sonucu
        viewModel.facebook.observe(viewLifecycleOwner) { (success, message) ->
            setLoading(false)
            showSnack(message.ifBlank { if (success) "Facebook ile giriş başarılı" else "Facebook ile giriş başarısız" })
            // success true ise yönlendirme
        }
    }

    //endregion

    //region Helpers

    @Deprecated("Facebook SDK için onActivityResult yönlendirmesi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::fbCallbackManager.isInitialized) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showSnack(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setLoading(loading: Boolean) {
        // RegisterFragment.xml’de eklediğimiz overlay ve içerik id’leri:
        // contentRoot, loadingOverlay, progressBar
        binding.loadingOverlay?.isVisible = loading

        // Android 12+ gerçek blur (isteğe bağlı)
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
        binding.registerBtn.isEnabled = !loading
        binding.registerGoogleBtn.isEnabled = !loading
        binding.registerFacebookBtn.isEnabled = !loading
        binding.registerEmailEditTxt.isEnabled = !loading
        binding.registerPasswordEditTxt.isEnabled = !loading
        binding.registerPasswordApproveEditTxt.isEnabled = !loading
        binding.progressBar?.isVisible = loading
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun validateInputs(): Boolean {
        val email = binding.registerEmailEditTxt.text?.toString()?.trim().orEmpty()
        val password = binding.registerPasswordEditTxt.text?.toString()?.trim().orEmpty()
        val confirm = binding.registerPasswordApproveEditTxt.text?.toString()?.trim().orEmpty()

        var ok = true

        if (email.isEmpty()) {
            binding.registerEmailLayout.error = "E-posta boş olamaz"
            ok = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.registerEmailLayout.error = "Geçersiz e-posta adresi"
            ok = false
        } else {
            binding.registerEmailLayout.error = null
        }

        if (password.length < 6) {
            binding.registerPasswordLayout.error = "Şifre en az 6 karakter olmalı"
            ok = false
        } else {
            binding.registerPasswordLayout.error = null
        }

        if (confirm != password) {
            binding.registerPasswordApproveLayout.error = "Şifreler uyuşmuyor"
            ok = false
        } else {
            binding.registerPasswordApproveLayout.error = null
        }

        return ok
    }

    //endregion

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
