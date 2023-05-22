package com.dicoding.doanda.storyapp.utils

import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val storyList = arrayListOf<ListStoryItem>()
        for (i in 0 until 100) {
            val story = ListStoryItem(
                "story-w594r3urmZ1zfiZO",
                "https://story-api.dicoding.dev/images/stories/photos-1684742310664_mKo8zjBG.jpg",
                "2023-05-22T07:58:30.675Z",
                "Doanda",
                "unit test",
                -7.272842,
                112.7653653,
            )
            storyList.add(story)
        }
        return storyList
    }
}