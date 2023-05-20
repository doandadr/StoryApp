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
import com.dicoding.doanda.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.doanda.storyapp.helper.RegisterViewModelFactory
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.models.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(pref))
            .get(RegisterViewModel::class.java)

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse?.error == false) {
                Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, registerResponse?.message ?: "error", Toast.LENGTH_SHORT).show()
            }
        }
        // cal' register api get response
        // if successful set preferences, intent go to login
        // set button to login

        binding.btnRegister.setOnClickListener { view ->
            if (view.id == R.id.btn_register) {
                // get fields
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()
                // verify fields TODO in custom edittext

                registerViewModel.registerRequest(name, email, password)
            }
        }

        binding.btnToLogin.setOnClickListener { view ->
            if (view.id == R.id.btn_to_register) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }
}