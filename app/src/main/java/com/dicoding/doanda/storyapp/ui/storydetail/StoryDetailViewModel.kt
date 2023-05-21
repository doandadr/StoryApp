package com.dicoding.doanda.storyapp.ui.storydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.ui.adapter.UserEntity
import com.dicoding.doanda.storyapp.data.source.remote.ApiConfig
import com.dicoding.doanda.storyapp.data.response.Story
import com.dicoding.doanda.storyapp.data.response.StoryDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDetailViewModel(private val pref: SessionPreferences):
    ViewModel() {

    private val _story = MutableLiveData<Story?>()
    val story: LiveData<Story?> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserEntity> = pref.getUser().asLiveData()

    fun loadStoryDetail(bearerToken: String, storyId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoryDetail(bearerToken, storyId)
        client.enqueue(object : Callback<StoryDetailResponse> {
            override fun onResponse(
                call: Call<StoryDetailResponse>,
                response: Response<StoryDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _story.value = responseBody.story
                    }
                } else {
                    Log.e(TAG, "onResponseUnsuccesful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "StoryDetailViewModel"
    }
}
