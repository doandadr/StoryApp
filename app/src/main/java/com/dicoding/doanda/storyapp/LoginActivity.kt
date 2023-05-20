package com.dicoding.doanda.storyapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.databinding.ActivityLoginBinding
import com.dicoding.doanda.storyapp.helper.LoginViewModelFactory
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.helper.UserEntity
import com.dicoding.doanda.storyapp.models.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(pref))
            .get(LoginViewModel::class.java)

        loginViewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse?.error == false) {
                val user = UserEntity(
                    loginResponse.loginResult?.name.toString(),
                    true,
                    "Bearer ${loginResponse.loginResult?.token}",
                )
                loginViewModel.saveUser(user)
            } else {
                Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Email or password incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.getUser().observe(this) { user ->
            val bearerToken = user.bearerToken
            if (bearerToken != "") {
                Toast.makeText(this@LoginActivity, "Login succesful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnLogin.setOnClickListener { view ->
            if (view.id == R.id.btn_login) {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                loginViewModel.loginRequest(email, password)
            }
        }

        binding.btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLoginLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvLoginDesc, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val edRegister = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btnToRegister = ObjectAnimator.ofFloat(binding.btnToRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, desc, edEmail, edRegister, btnLogin, btnToRegister)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLogin.visibility = View.VISIBLE
        } else {
            binding.pbLogin.visibility = View.GONE
        }
    }
}