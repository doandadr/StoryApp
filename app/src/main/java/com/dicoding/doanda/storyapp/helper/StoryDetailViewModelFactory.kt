package com.dicoding.doanda.storyapp.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.models.StoryDetailViewModel

class StoryDetailViewModelFactory(private val pref: SessionPreferences, private val storyId: String) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModelFactory::class.java)) {
            return StoryDetailViewModel(pref, storyId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
