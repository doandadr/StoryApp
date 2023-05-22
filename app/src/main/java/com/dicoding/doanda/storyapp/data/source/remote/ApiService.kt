package com.dicoding.doanda.storyapp.data.source.remote

import com.dicoding.doanda.storyapp.data.response.AllStoriesResponse
import com.dicoding.doanda.storyapp.data.response.LoginResponse
import com.dicoding.doanda.storyapp.data.response.RegisterResponse
import com.dicoding.doanda.storyapp.data.response.StoryDetailResponse
import com.dicoding.doanda.storyapp.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String, 
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?,
    ): UploadStoryResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String, 
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
    ): AllStoriesResponse

    @GET("stories")
    suspend fun getAllStoriesLocation(
        @Header("Authorization") token: String, 
        @Query("location") location: Int,
    ): AllStoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String, 
        @Path("id") id : String,
    ): StoryDetailResponse
}

