package com.alperendiler.mine.video

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alperendiler.mine.R
import com.alperendiler.mine.adapter.FeedRecyclerAdapter
import com.alperendiler.mine.databinding.ActivityStartBinding
import com.alperendiler.mine.databinding.ActivityVideoPlayerBinding
import com.alperendiler.mine.model.Post
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class VideoPlayer: AppCompatActivity(){

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var exoplayer: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth







        initializePlayer()
    }

    private fun initializePlayer() {
        // Initialize ExoPlayer
        exoplayer = SimpleExoPlayer.Builder(this)
            .build()

        // Initialize PlayerNotificationManager
        initPlayerNotificationManager()

        // Set the exoPlayer to the playerView
        binding.videoView.player = exoplayer

        // Create a MediaItem
        val mediaItem = createMediaItem()

        exoplayer.addMediaItem(mediaItem)
        exoplayer.prepare()
        exoplayer.play()
    }

    private fun createMediaItem(): MediaItem {
        val mediaUri = Uri.parse("gs://mine-3e616.appspot.com/")
        return MediaItem.fromUri(mediaUri)
    }

    private fun initPlayerNotificationManager() {
        if(Build.VERSION.SDK_INT >= 26) {   val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL, Constants.NOTIFICATION_CHANNEL, IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        }

        playerNotificationManager = PlayerNotificationManager.Builder(this,
            Constants.NOTIFICATION_ID,
            Constants.NOTIFICATION_CHANNEL,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence =
                    player.currentMediaItem?.mediaMetadata?.title ?: "Music"

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return "Music Content Text"
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_play_button)
                }

            })
            .build()
        playerNotificationManager.setPlayer(exoplayer)
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoplayer.stop()
        playerNotificationManager.setPlayer(null)
        exoplayer.release()
    }



//
//    private var mPlayer: SimpleExoPlayer? = null
//    private lateinit var playerView: PlayerView
//    private lateinit var db : FirebaseFirestore
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var binding : ActivityVideoPlayerBinding
//    private val videoURL =
//        "gs://mine-3e616.appspot.com/voice/3e5618b1-22ed-4418-abae-77552fc0b19f.mp4"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        //get PlayerView by its id
//        playerView = findViewById(R.id.recyclerImageView)
//
//
//        auth = Firebase.auth
//        db = Firebase.firestore
//    }
//
//    private fun initPlayer() {
//
//        // Create a player instance.
//        mPlayer = SimpleExoPlayer.Builder(this).build()
//
//        // Bind the player to the view.
//        playerView.player = mPlayer
//
//        //setting exoplayer when it is ready.
//        mPlayer!!.playWhenReady = true
//
//        // Set the media source to be played.
//        mPlayer!!.setMediaSource(buildMediaSource())
//
//        // Prepare the player.
//        mPlayer!!.prepare()
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (Util.SDK_INT >= 24) {
//            initPlayer()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (Util.SDK_INT < 24 || mPlayer == null) {
//            initPlayer()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (Util.SDK_INT < 24) {
//            releasePlayer()
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if (Util.SDK_INT >= 24) {
//            releasePlayer()
//        }
//    }
//
//
//    private fun releasePlayer() {
//        if (mPlayer == null) {
//            return
//        }
//        //release player when done
//        mPlayer!!.release()
//        mPlayer = null
//    }
//
//    //creating mediaSource
//    private fun buildMediaSource(): MediaSource {
//        // Create a data source factory.
//        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
//
//        // Create a progressive media source pointing to a stream uri.
//        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(videoURL))
//
//        return mediaSource
//    }
}

