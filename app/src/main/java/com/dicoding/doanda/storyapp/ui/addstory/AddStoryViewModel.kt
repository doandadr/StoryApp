package com.dicoding.doanda.storyapp.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.ui.adapter.UserEntity
import com.dicoding.doanda.storyapp.data.source.remote.ApiConfig
import com.dicoding.doanda.storyapp.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref : SessionPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadStoryResponse = MutableLiveData<UploadStoryResponse?>()
    val uploadStoryResponse: LiveData<UploadStoryResponse?> = _uploadStoryResponse

    fun getUser(): LiveData<UserEntity> = pref.getUser().asLiveData()
    fun uploadStory(
        bearerToken: String,
        desc: RequestBody,
        imageMultipart: MultipartBody.Part,
        lat: Float?,
        lon: Float?
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory(bearerToken, desc, imageMultipart, lat, lon)
        client.enqueue(object : Callback<UploadStoryResponse> {
            override fun onResponse(
                call: Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _uploadStoryResponse.value = responseBody
                    }
                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onResponseUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}
