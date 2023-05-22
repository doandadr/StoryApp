package com.dicoding.doanda.storyapp.ui.story

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import kotlinx.coroutines.launch

class StoryViewModel(private val repo: StoryRepository) : ViewModel() {
    fun getUser(): LiveData<UserEntity> =
        repo.getUser()

    fun getAllStories(token: String) : LiveData<PagingData<ListStoryItem>> =
        repo.getAllStories(token).cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}
