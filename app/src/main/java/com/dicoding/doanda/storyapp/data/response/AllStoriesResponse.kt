package com.dicoding.doanda.storyapp.data.response

import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem
import com.google.gson.annotations.SerializedName

data class AllStoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

