package com.dicoding.doanda.storyapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.helper.UserEntity
import com.dicoding.doanda.storyapp.network.ApiConfig
import com.dicoding.doanda.storyapp.network.UploadStoryResponse
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
//        val dummyBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWt0UkJraTFLNjhORjB5TXUiLCJpYXQiOjE2ODMxNzE2NjN9.YgGQYTfb8k_S3JW0gbR0ySEsNfqqfHqTDhDpnF1zvA0"
//        val client = ApiConfig.getApiService().uploadStory(dummyBearerToken, desc, imageMultipart, lat, lon)
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
