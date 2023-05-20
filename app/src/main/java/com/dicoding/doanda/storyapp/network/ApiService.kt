package com.dicoding.doanda.storyapp.network

import com.dicoding.doanda.storyapp.AllStoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @Headers("Content-type: multipart/form-data")
    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String, // Bearer + token
        @Field("description") description: String,
        @Part("file") file: MultipartBody.Part,
        @Field("lat") lat: Float,
        @Field("lon") lon: Float,
    ): Call<AddNewStoryResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String?, // Bearer + token
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

