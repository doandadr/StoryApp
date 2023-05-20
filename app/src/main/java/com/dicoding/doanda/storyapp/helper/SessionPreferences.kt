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

    fun getIsLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
//            preferences[IS_LOGGED_IN] ?: true
        }
    }

    fun getBearerToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BEARER_TOKEN] ?: ""
//            preferences[BEARER_TOKEN] ?: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWt0UkJraTFLNjhORjB5TXUiLCJpYXQiOjE2ODMxNzE2NjN9.YgGQYTfb8k_S3JW0gbR0ySEsNfqqfHqTDhDpnF1zvA0"
        }
    }

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveBearerToken(bearerToken: String) {
        dataStore.edit { preferences ->
            preferences[BEARER_TOKEN] = bearerToken
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