package com.dicoding.doanda.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.doanda.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.doanda.storyapp.models.AddStoryViewModel

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}