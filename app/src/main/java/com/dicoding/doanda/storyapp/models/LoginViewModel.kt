package com.dicoding.doanda.storyapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.network.ApiConfig
import com.dicoding.doanda.storyapp.network.LoginResponse
import com.dicoding.doanda.storyapp.network.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SessionPreferences) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    fun saveBearerToken(token: String) {
        viewModelScope.launch {
            pref.saveBearerToken("Bearer $token")
        }
    }

    fun saveIsLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.saveIsLoggedIn(isLoggedIn)
        }
    }

    fun loginRequest(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _loginResponse.value = responseBody
                    }
                } else {
                    _loginResponse.value = response.body()
                    Log.e(TAG, "onResponseUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
