package com.alperendiler.mine.adapter

import android.view.KeyCharacterMap.load
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alperendiler.mine.databinding.ReycylerViewRowBinding
import com.alperendiler.mine.model.Post
import com.alperendiler.mine.model.VideoPost
import com.google.android.exoplayer2.SimpleExoPlayer

import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.ParsingLoadable.load
import com.google.android.exoplayer2.util.Log
import io.grpc.InternalServiceProviders.load
import java.lang.System.load
import java.lang.reflect.Array.get
import java.nio.file.Paths.get


class VideoPlayerAdapter(private val postList : ArrayList<VideoPost>) : RecyclerView.Adapter<VideoPlayerAdapter.PostHolder>() {
    class PostHolder(val binding: ReycylerViewRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val playerView: PlayerView? = null
    private val player: SimpleExoPlayer? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding =
            ReycylerViewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoPlayerAdapter.PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return postList.size
    }

}