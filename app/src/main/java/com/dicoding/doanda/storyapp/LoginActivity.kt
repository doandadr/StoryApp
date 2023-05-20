package com.dicoding.doanda.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.databinding.ActivityLoginBinding
import com.dicoding.doanda.storyapp.helper.LoginViewModelFactory
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.models.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        // cal login api get response
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(pref))
            .get(LoginViewModel::class.java)

        loginViewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse?.error == false) {
                loginViewModel.saveIsLoggedIn(true)
                loginViewModel.saveBearerToken(loginResponse.loginResult?.token.toString())

                Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, loginResponse?.message ?: "error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener { view ->
            if (view.id == R.id.btn_login) {
                // get fields
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                // verify fields TODO put in custom edittext

                loginViewModel.loginRequest(email, password)
            }
        }

        binding.btnToRegister.setOnClickListener { view ->
            if (view.id == R.id.btn_to_register) {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }
}