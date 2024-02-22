package com.example.cloneinstagramudemy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneinstagramudemy.databinding.ItemPostBinding
import com.example.cloneinstagramudemy.model.Posts
import com.squareup.picasso.Picasso

class FeedAdapter(private val postsArray : ArrayList<Posts>) : RecyclerView.Adapter<FeedAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount() = postsArray.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = postsArray[position]
        holder.binding.userEmailItem.text = post.userEmail
        holder.binding.postDescriptionItem.text = post.description
        holder.binding.dateItem.text = post.time
        Picasso
            .get()
            .load(post.imageUrl)
            .into(holder.binding.postImageItem)

    }

    class PostHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root){

    }
}
