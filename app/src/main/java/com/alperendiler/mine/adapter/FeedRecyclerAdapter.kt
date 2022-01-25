package com.alperendiler.mine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alperendiler.mine.databinding.ReycylerViewRowBinding
import com.alperendiler.mine.model.Post
import com.squareup.picasso.Picasso


class FeedRecyclerAdapter(private val postList : ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: ReycylerViewRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val binding =
            ReycylerViewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostHolder(binding)

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.binding.recyclerCommentText.text = postList.get(position).email
        holder.binding.recyclerCommentText.text = postList.get(position).comment
        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.recyclerImageView)

    }

    override fun getItemCount(): Int {


        return postList.size

    }
}