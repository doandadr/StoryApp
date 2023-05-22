package com.dicoding.doanda.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.storyapp.R
import com.dicoding.doanda.storyapp.data.paging.LoadingStateAdapter
import com.dicoding.doanda.storyapp.data.paging.StoryAdapter
import com.dicoding.doanda.storyapp.databinding.ActivityStoryBinding
import com.dicoding.doanda.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.doanda.storyapp.ui.login.LoginActivity
import com.dicoding.doanda.storyapp.ui.maps.MapsActivity
import com.dicoding.doanda.storyapp.ui.utils.factory.ViewModelFactory

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {ViewModelFactory.getInstance(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        getStories()

        binding.ibLogout.setOnClickListener {
            storyViewModel.logout()
        }
        binding.ibMaps.setOnClickListener {
            startActivity(Intent(this@StoryActivity, MapsActivity::class.java))
        }
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@StoryActivity, AddStoryActivity::class.java))
        }
    }

    private fun getStories() {
        storyViewModel.getUser().observe(this) { user ->
            if (user.isLoggedIn) {
                val adapter = StoryAdapter()
                binding.rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                storyViewModel.getAllStories(user.bearerToken).observe(this) {
                    adapter.submitData(lifecycle, it)
                }

                binding.tvMainDesc.text = getString(R.string.check_out_new_stories, user.userName)
            } else {
                val intent = Intent(this@StoryActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}