package com.dicoding.doanda.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.doanda.storyapp.data.repository.StoryRepository

class RegisterViewModel(private val repo: StoryRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repo.register(name, email, password)
}
