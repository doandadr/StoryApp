package com.dicoding.doanda.storyapp.network

import com.google.gson.annotations.SerializedName

data class AddNewStoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
