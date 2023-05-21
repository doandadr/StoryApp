package com.dicoding.doanda.storyapp.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.doanda.storyapp.data.response.AllStoriesResponse
import com.dicoding.doanda.storyapp.data.response.ListStoryItem
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import com.dicoding.doanda.storyapp.data.source.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref : SessionPreferences) : ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem?>?>()
    val listStory: LiveData<List<ListStoryItem?>?> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserEntity> = pref.getUser().asLiveData()

    fun getAllStoriesLocation(bearerToken: String) {
        _isLoading.value = true
        val location = 1
        val client = ApiConfig.getApiService().getAllStoriesLocation(bearerToken, location)
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
                    Log.e(TAG, "onResponseUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}