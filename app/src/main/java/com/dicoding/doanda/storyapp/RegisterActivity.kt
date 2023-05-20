package com.dicoding.doanda.storyapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.doanda.storyapp.helper.RegisterViewModelFactory
import com.dicoding.doanda.storyapp.models.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerResponse.observe(this) { registerResponse ->
            if (registerResponse?.error == false) {
                Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, registerResponse?.message ?: "Email or password invalid", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnRegister.setOnClickListener { view ->
            if (view.id == R.id.btn_register) {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

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

        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        playAnimation()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvRegisterDesc, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val btnToLogin = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                name,
                email,
                password,
                btnRegister,
                btnToLogin,
            )
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.pbRegister.visibility = View.GONE
        }
    }
}