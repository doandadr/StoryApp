package com.dicoding.doanda.storyapp

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
import com.dicoding.doanda.storyapp.databinding.ActivityMainBinding
import com.dicoding.doanda.storyapp.helper.MainViewModelFactory
import com.dicoding.doanda.storyapp.helper.SessionPreferences
import com.dicoding.doanda.storyapp.helper.StoryListAdapter
import com.dicoding.doanda.storyapp.models.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))
            .get(MainViewModel::class.java)

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLoggedIn) {
                binding.tvMainDesc.text = getString(R.string.check_out_new_stories, user.userName)
                mainViewModel.getAllStories(user.bearerToken)
            } else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        mainViewModel.listStory.observe(this) { listStory ->
            setStoryListData(listStory)
        }
        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.ibLogout.setOnClickListener {
            mainViewModel.logout()
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
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
                    val intent = Intent(this@MainActivity, StoryDetailActivity::class.java)
                    intent.putExtra(StoryDetailActivity.EXTRA_LIST_STORY_ITEM, storyId)
                    startActivity(intent)
                }
            })
        }
    }


}