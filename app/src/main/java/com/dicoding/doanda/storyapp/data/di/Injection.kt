package com.dicoding.doanda.storyapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.doanda.storyapp.data.repository.StoryRepository
import com.dicoding.doanda.storyapp.data.source.database.StoryDatabase
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.data.source.remote.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = SessionPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(
            pref = preferences,
            apiService = apiService,
            database = database,
        )
    }
}