package com.dicoding.doanda.storyapp.ui.storydetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.storyapp.data.repository.Result
import com.dicoding.doanda.storyapp.data.response.Story
import com.dicoding.doanda.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.doanda.storyapp.ui.utils.factory.ViewModelFactory

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private val storyDetailViewModel by viewModels<StoryDetailViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Detail"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyId = intent.getStringExtra(EXTRA_LIST_STORY_ITEM)
        if (storyId != null) {
            getStoryDetail(storyId)
        }
    }

    private fun getStoryDetail(storyId: String) {
        storyDetailViewModel.getUser().observe(this) {user ->
            if (user.isLoggedIn) {
                storyDetailViewModel.getStoryDetail(user.bearerToken, storyId).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            setStoryDetail(result.data.story)
                        }
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> {
                            Toast.makeText(this@StoryDetailActivity, result.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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