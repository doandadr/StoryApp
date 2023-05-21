package com.dicoding.doanda.storyapp.ui.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.storyapp.*
import com.dicoding.doanda.storyapp.data.response.ListStoryItem
import com.dicoding.doanda.storyapp.databinding.ActivityStoryBinding
import com.dicoding.doanda.storyapp.data.source.local.SessionPreferences
import com.dicoding.doanda.storyapp.ui.adapter.StoryListAdapter
import com.dicoding.doanda.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.doanda.storyapp.ui.login.LoginActivity
import com.dicoding.doanda.storyapp.ui.storydetail.StoryDetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        storyViewModel = ViewModelProvider(this, StoryViewModelFactory(pref))
            .get(StoryViewModel::class.java)

        storyViewModel.getUser().observe(this) { user ->
            if (user.isLoggedIn) {
                binding.tvMainDesc.text = getString(R.string.check_out_new_stories, user.userName)
                storyViewModel.getAllStories(user.bearerToken)
            } else {
                val intent = Intent(this@StoryActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        storyViewModel.listStory.observe(this) { listStory ->
            setStoryListData(listStory)
        }
        storyViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.ibLogout.setOnClickListener {
            storyViewModel.logout()
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@StoryActivity, AddStoryActivity::class.java))
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