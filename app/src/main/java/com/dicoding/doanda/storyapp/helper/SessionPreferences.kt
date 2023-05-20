package com.dicoding.doanda.storyapp.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val BEARER_TOKEN = stringPreferencesKey("bearer_token")
    private val USER_NAME = stringPreferencesKey("name")


    fun getUser(): Flow<UserEntity> {
        return dataStore.data.map { preferences ->
            UserEntity(
                preferences[USER_NAME] ?: "",
                preferences[IS_LOGGED_IN] ?: false,
                preferences[BEARER_TOKEN] ?: "",
            )
        }
    }

    suspend fun saveUser(user: UserEntity) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = user.userName
            preferences[IS_LOGGED_IN] = user.isLoggedIn
            preferences[BEARER_TOKEN] = user.bearerToken
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}