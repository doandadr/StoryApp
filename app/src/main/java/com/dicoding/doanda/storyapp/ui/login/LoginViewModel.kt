package com.dicoding.doanda.storyapp.ui.login

import androidx.lifecycle.*
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: StoryRepository) : ViewModel() {
    fun login(email: String, password: String) =
        repo.login(email, password)

    fun getUser(): LiveData<UserEntity> =
        repo.getUser()

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            repo.saveUser(user)
        }
    }
}
