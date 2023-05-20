package com.dicoding.doanda.storyapp.models

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.doanda.storyapp.AllStoriesResponse
import com.dicoding.doanda.storyapp.ListStoryItem
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.helper.UserEntity
import com.dicoding.doanda.storyapp.network.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref : SessionPreferences) : ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem?>?>()
    val listStory: LiveData<List<ListStoryItem?>?> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserEntity> = pref.getUser().asLiveData()

    fun getAllStories(bearerToken: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStories(bearerToken, null, null, null)
//        val client = ApiConfig.getApiService().getAllStories(bearerToken.value, 1, 10, null)
//        val dummyBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWt0UkJraTFLNjhORjB5TXUiLCJpYXQiOjE2ODMxNzE2NjN9.YgGQYTfb8k_S3JW0gbR0ySEsNfqqfHqTDhDpnF1zvA0"
//        val client = ApiConfig.getApiService().getAllStories(dummyBearerToken, 1, 10, null)
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listStory.value = responseBody.listStory
                    }
                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onResponseUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })

    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
    companion object {
        private const val TAG = "MainViewModel"
    }
}
