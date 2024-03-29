package com.dicoding.doanda.storyapp.data.response

import com.dicoding.doanda.storyapp.data.response.partials.LoginResult
import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

