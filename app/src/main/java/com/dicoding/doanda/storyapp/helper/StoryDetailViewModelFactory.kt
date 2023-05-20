package com.dicoding.doanda.storyapp.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.models.StoryDetailViewModel

class StoryDetailViewModelFactory(private val pref: SessionPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
