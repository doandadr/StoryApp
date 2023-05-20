package com.dicoding.doanda.storyapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.helper.StoryDetailViewModelFactory
import com.dicoding.doanda.storyapp.models.StoryDetailViewModel
import com.dicoding.doanda.storyapp.network.Story

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var storyDetailViewModel: StoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Detail"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = SessionPreferences.getInstance(dataStore)

        val storyId = intent.getStringExtra(EXTRA_LIST_STORY_ITEM)
        if (storyId != null) {
            storyDetailViewModel = ViewModelProvider(this, StoryDetailViewModelFactory(pref))
                .get(StoryDetailViewModel::class.java)
        }

        storyDetailViewModel.story.observe(this) {story ->
            setStoryDetail(story)
        }

        storyDetailViewModel.isLoading.observe(this) {isLoading ->
            showLoading(isLoading)
        }

        storyDetailViewModel.getUser().observe(this) {user ->
            if (user.isLoggedIn) {
                if (storyId != null)
                    storyDetailViewModel.loadStoryDetail(user.bearerToken, storyId)
            }
        }
    }

    private fun setStoryDetail(story: Story?) {
        val requestOptions = RequestOptions().transform(FitCenter(), RoundedCorners(50))

        Glide.with(this@StoryDetailActivity)
            .load(story?.photoUrl)
            .apply(requestOptions)
            .skipMemoryCache(true)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story?.name
        binding.tvDetailDescription.text = story?.description
    }


    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_LIST_STORY_ITEM: String = "extra_list_story_item"
    }
}