package com.dicoding.doanda.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.local.UserEntity

class MapsViewModel(private val repo : StoryRepository) : ViewModel() {
    fun getUser(): LiveData<UserEntity> = repo.getUser()

    fun getAllStoriesLocation(token: String, location: Int) =
        repo.getAllStoriesLocation(token, location)
}