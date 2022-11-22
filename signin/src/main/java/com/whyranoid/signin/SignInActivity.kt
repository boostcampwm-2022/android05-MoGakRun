package com.whyranoid.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.whyranoid.SignInUserInfo
import com.whyranoid.SignInViewModel
import com.whyranoid.presentation.MainActivity
import com.whyranoid.signin.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private var uid: String? = null
    private var email: String? = null
    private var nickName: String? = null
    private var profileImgUri: String? = null
    private val viewModel by viewModels<SignInViewModel>()

    // 구글 로그인 클라이언트 객체
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.token)
                .requestEmail()
                .requestProfile()
                .build()
        )
    }

    // 구글 로그인 클라이언트 런처 객체
    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    account.idToken?.let { idToken ->
                        signInWithGoogle(idToken)
                        // 데이터 스토어에 유저 정보 저장
                        email = account.email
                        nickName = account.displayName
                        profileImgUri = account.photoUrl.toString()
                    }
                } catch (e: ApiException) {
                    Snackbar.make(
                        binding.constraintLayoutSignIn,
                        getString(R.string.sign_in_fail_network),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Snackbar.make(
                    binding.constraintLayoutSignIn,
                    getString(R.string.sign_in_fail_cancel),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        auth = Firebase.auth

        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        initView()
    }

    private fun initView() {
        binding.btnGoogleSignIn.setOnClickListener {
            launchGoogleSignInClient()
        }
    }

    // 구글 로그인 클라이언트를 런칭시키는 함수
    private fun launchGoogleSignInClient() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    // 구글에 연결된 아이디를 파이어베이스에 적용
    private fun signInWithGoogle(idToken: String) {
        val intent = Intent(this, MainActivity::class.java)
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snackbar.make(
                    binding.constraintLayoutSignIn,
                    getString(R.string.sign_in_success),
                    Snackbar.LENGTH_SHORT
                ).show()

                uid = auth.uid
                lifecycleScope.launch {
                    uid?.let { uid ->
                        viewModel.saveUserInfo(SignInUserInfo(uid, email, nickName, profileImgUri))
                    }
                }

                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.constraintLayoutSignIn,
                    getString(R.string.sign_in_fail_network),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
