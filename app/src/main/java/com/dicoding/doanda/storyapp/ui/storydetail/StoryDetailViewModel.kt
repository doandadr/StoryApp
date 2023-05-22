package com.dicoding.doanda.storyapp.ui.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.local.UserEntity

class StoryDetailViewModel(private val repo: StoryRepository):
    ViewModel() {

    fun getUser(): LiveData<UserEntity> =
        repo.getUser()

    fun getStoryDetail(token: String, storyId: String) =
        repo.getStoryDetail(token, storyId)

}
