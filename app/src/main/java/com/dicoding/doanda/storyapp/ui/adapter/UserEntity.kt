package com.dicoding.doanda.storyapp.ui.adapter

data class UserEntity(
    val userName: String,
    val isLoggedIn: Boolean,
    val bearerToken: String,
)