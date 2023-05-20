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


class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // TODO set theme
        setContentView(binding.root)

        // TODO get set preferences login status
        val pref = SessionPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))
            .get(MainViewModel::class.java)

        // TODO observe model
        mainViewModel.isLoggedIn.observe(this) { isLoggedIn ->
//            val dummyIsLoggedIn = true
            if (isLoggedIn) {
                mainViewModel.getAllStories()
            } else {
                // TODO intent to login
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
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

    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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