package com.dicoding.doanda.storyapp.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.storyapp.*
import com.dicoding.doanda.storyapp.data.repository.Result
import com.dicoding.doanda.storyapp.data.response.ListStoryItem
import com.dicoding.doanda.storyapp.databinding.ActivityStoryBinding
import com.dicoding.doanda.storyapp.ui.utils.adapter.StoryListAdapter
import com.dicoding.doanda.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.doanda.storyapp.ui.login.LoginActivity
import com.dicoding.doanda.storyapp.ui.maps.MapsActivity
import com.dicoding.doanda.storyapp.ui.storydetail.StoryDetailActivity
import com.dicoding.doanda.storyapp.ui.utils.factory.ViewModelFactory

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {ViewModelFactory.getInstance(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // TODO PAGING
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
                binding.tvMainDesc.text = getString(R.string.check_out_new_stories, user.userName)
                storyViewModel.getAllStories(user.bearerToken, null, null, null)
                    .observe(this) { result ->
                        when (result) {
                            is Result.Success -> {
                                showLoading(false)
                                setStoryListData(result.data.listStory)
                            }
                            is Result.Loading -> showLoading(true)
                            is Result.Error -> {
                                Toast.makeText(this@StoryActivity, result.error, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }
                    }
            } else {
                val intent = Intent(this@StoryActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.pbMain.visibility = View.VISIBLE
        } else {
            binding.pbMain.visibility = View.GONE
        }
    }

    private fun setStoryListData(items: List<ListStoryItem?>?) {
        if (items != null && items.isNotEmpty()) {
            val adapter = StoryListAdapter(items.filterNotNull())
            binding.rvStories.adapter = adapter
            adapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback {
                override fun onItemClicked(listStoryItem: ListStoryItem) {
                    val storyId = listStoryItem.id
                    val intent = Intent(this@StoryActivity, StoryDetailActivity::class.java)
                    intent.putExtra(StoryDetailActivity.EXTRA_LIST_STORY_ITEM, storyId)
                    startActivity(intent)
                }
            })
        }
    }


}