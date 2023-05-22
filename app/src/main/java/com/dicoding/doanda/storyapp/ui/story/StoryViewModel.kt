package com.dicoding.doanda.storyapp.ui.story

import androidx.lifecycle.*
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import kotlinx.coroutines.launch

class StoryViewModel(private val repo: StoryRepository) : ViewModel() {
    fun getUser(): LiveData<UserEntity> =
        repo.getUser()

    fun getAllStories(token: String, page: Int?, size: Int?, location: Int?) =
        repo.getAllStories(token, page, size, location)

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}
