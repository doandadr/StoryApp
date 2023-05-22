package com.dicoding.doanda.storyapp.ui.utils.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.doanda.storyapp.data.di.Injection
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.ui.addstory.AddStoryViewModel
import com.dicoding.doanda.storyapp.ui.login.LoginViewModel
import com.dicoding.doanda.storyapp.ui.maps.MapsViewModel
import com.dicoding.doanda.storyapp.ui.register.RegisterViewModel
import com.dicoding.doanda.storyapp.ui.story.StoryViewModel
import com.dicoding.doanda.storyapp.ui.storydetail.StoryDetailViewModel

class ViewModelFactory(private val repo: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) ->
                StoryViewModel(repo) as T
            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) ->
                StoryDetailViewModel(repo) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(repo) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(repo) as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) ->
                AddStoryViewModel(repo) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) ->
                MapsViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}