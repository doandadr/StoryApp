package com.dicoding.doanda.storyapp.ui.utils.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.ui.addstory.AddStoryViewModel
import com.dicoding.doanda.storyapp.ui.login.LoginViewModel
import com.dicoding.doanda.storyapp.ui.maps.MapsViewModel
import com.dicoding.doanda.storyapp.ui.register.RegisterViewModel
import com.dicoding.doanda.storyapp.ui.story.StoryViewModel
import com.dicoding.doanda.storyapp.ui.storydetail.StoryDetailViewModel

class ViewModelFactory(private val pref: SessionPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) ->
                StoryViewModel(pref) as T
            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) ->
                StoryDetailViewModel(pref) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(pref) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel() as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) ->
                AddStoryViewModel(pref) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) ->
                MapsViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}