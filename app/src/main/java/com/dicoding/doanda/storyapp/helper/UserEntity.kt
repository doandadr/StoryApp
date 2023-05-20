package com.dicoding.doanda.storyapp.helper

data class UserEntity(
    val userName: String,
    val isLoggedIn: Boolean,
    val bearerToken: String,
)