package com.codandotv.streamplayerapp.core_shared_ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.codandotv.streamplayerapp.core_shared_ui.theme.ThemePreviews
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun PlayerComponent(videoId: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val youtubePlayerView = remember {
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false

            addYouTubePlayerListener(object : YouTubePlayerListener {
                override fun onApiChange(youTubePlayer: YouTubePlayer) = Unit

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) = Unit

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) = Unit

                override fun onPlaybackQualityChange(
                    youTubePlayer: YouTubePlayer,
                    playbackQuality: PlayerConstants.PlaybackQuality
                ) = Unit

                override fun onPlaybackRateChange(
                    youTubePlayer: YouTubePlayer,
                    playbackRate: PlayerConstants.PlaybackRate
                ) = Unit

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) = Unit

                override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) = Unit

                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) = Unit

                override fun onVideoLoadedFraction(
                    youTubePlayer: YouTubePlayer,
                    loadedFraction: Float
                ) = Unit
            })
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.TopCenter),
                factory = {
                    youtubePlayerView
                }
            )
        }
    }

    DisposableEffect(
        key1 = Unit,
        effect = {
            onDispose { youtubePlayerView.release() }
        },
    )
}

@Composable
@ThemePreviews
fun PlayerComponentPreview() {
    PlayerComponent(
        videoId = "BigBuckBunny.mp4",
        modifier = Modifier.fillMaxWidth()
    )
}
