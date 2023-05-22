package com.dicoding.doanda.storyapp.data.paging

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem
import com.dicoding.doanda.storyapp.databinding.ItemStoryBinding
import com.dicoding.doanda.storyapp.ui.storydetail.StoryDetailActivity

class StoryAdapter
    : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK)
{

    class StoryViewHolder(private val binding: ItemStoryBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ListStoryItem) {
                with(binding) {
                    tvItemName.text = item.name
                    var requestOptions = RequestOptions()
                    requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
                    Glide.with(itemView.context)
                        .load(item.photoUrl)
                        .apply(requestOptions)
                        .into(ivItemPhoto)

                }
                itemView.setOnClickListener {
                    binding.root.context.startActivity(
                        Intent(
                            binding.root.context, StoryDetailActivity::class.java
                        ).putExtra(StoryDetailActivity.EXTRA_LIST_STORY_ITEM, item.id)
                    )
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem)=
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem)=
                oldItem.id == newItem.id
        }
    }

}