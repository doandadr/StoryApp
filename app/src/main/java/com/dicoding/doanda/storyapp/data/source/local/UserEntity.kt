package com.dicoding.doanda.storyapp.data.source.local

data class UserEntity(
    val userName: String,
    val isLoggedIn: Boolean,
    val bearerToken: String,
)