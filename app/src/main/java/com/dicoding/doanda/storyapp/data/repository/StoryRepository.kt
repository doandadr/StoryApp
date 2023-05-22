package com.dicoding.doanda.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.doanda.storyapp.data.response.*
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.data.source.local.UserEntity
import com.dicoding.doanda.storyapp.data.source.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository (private val pref: SessionPreferences, private val apiService: ApiService) {

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> =
    liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "Register ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> =
    liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "Login ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser() : LiveData<UserEntity> {
        return pref.getUser().asLiveData()
    }

    suspend fun saveUser(user: UserEntity) {
        pref.saveUser(user)
    }

    suspend fun logout()  {
        pref.logout()
    }

    // TODO PAGING
    fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?,
    ) : LiveData<Result<AllStoriesResponse>> =
    liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories(token, page, size, location)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "getAllStories ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStoriesLocation(token: String, location: Int) : LiveData<Result<AllStoriesResponse>>
    = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStoriesLocation(token, location)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "getAllStoriesLocation ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryDetail(token: String, storyId: String) : LiveData<Result<StoryDetailResponse>> =
    liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryDetail(token, storyId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "getStoryDetail ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        lat: Float?,
        lon: Float?,
    ) : LiveData<Result<UploadStoryResponse>> =
    liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(token, description, file, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e(TAG, "uploadStory ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "StoryRepository"
    }
}