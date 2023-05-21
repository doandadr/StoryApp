package com.dicoding.doanda.storyapp.data.source.remote

import com.dicoding.doanda.storyapp.data.response.AllStoriesResponse
import com.dicoding.doanda.storyapp.data.response.LoginResponse
import com.dicoding.doanda.storyapp.data.response.RegisterResponse
import com.dicoding.doanda.storyapp.data.response.StoryDetailResponse
import com.dicoding.doanda.storyapp.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String, // Bearer + token
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?,
    ): Call<UploadStoryResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String, // Bearer + token
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Boolean?,
    ): Call<AllStoriesResponse>

    @GET("stories/{id}")
    fun getStoryDetail(
        @Header("Authorization") token: String, // Bearer + token
        @Path("id") id : String,
    ): Call<StoryDetailResponse>
}

