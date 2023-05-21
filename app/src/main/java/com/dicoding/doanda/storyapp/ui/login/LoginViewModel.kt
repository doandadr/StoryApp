package com.dicoding.doanda.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import com.dicoding.doanda.storyapp.data.source.remote.ApiConfig
import com.dicoding.doanda.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SessionPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    fun getUser(): LiveData<UserEntity> = pref.getUser().asLiveData()
    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun loginRequest(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
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
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
