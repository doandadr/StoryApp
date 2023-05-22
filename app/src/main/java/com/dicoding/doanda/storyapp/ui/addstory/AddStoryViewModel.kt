package com.dicoding.doanda.storyapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repo: StoryRepository) : ViewModel() {
    fun getUser(): LiveData<UserEntity> =
        repo.getUser()

    fun uploadStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        lat: Double?,
        lon: Double?
    ) = repo.uploadStory(token, description, file, lat, lon)
}
