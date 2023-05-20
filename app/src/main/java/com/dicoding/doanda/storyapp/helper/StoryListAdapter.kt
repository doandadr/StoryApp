package com.dicoding.doanda.storyapp.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.storyapp.ListStoryItem
import com.dicoding.doanda.storyapp.R

class StoryListAdapter(private val listStory: List<ListStoryItem>) :
RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private lateinit var  onItemClickCallback: OnItemClickCallback

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tv_item_name)
        val ivItemPhoto: ImageView = view.findViewById(R.id.iv_item_photo)
    }

    interface OnItemClickCallback {
        fun onItemClicked(listStoryItem: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUrl = listStory[position].photoUrl
        val name = listStory[position].name

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .apply(requestOptions)
            .skipMemoryCache(true)
            .into(holder.ivItemPhoto)
        holder.tvItemName.text = name

        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listStory[holder.bindingAdapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = listStory.size


}