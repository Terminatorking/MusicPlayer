package ghazimoradi.soheil.musicplayer.ui.screens.player

import android.content.ContentUris
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.RepeatOne
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.linc.amplituda.Amplituda
import com.linc.amplituda.Cache
import ghazimoradi.soheil.musicplayer.R
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.ui.components.WaveformBar
import ghazimoradi.soheil.musicplayer.ui.theme.Bayside
import ghazimoradi.soheil.musicplayer.ui.theme.Charade
import ghazimoradi.soheil.musicplayer.ui.theme.EerieBlack
import ghazimoradi.soheil.musicplayer.ui.theme.FrostBlack
import ghazimoradi.soheil.musicplayer.ui.theme.Vermillion
import ghazimoradi.soheil.musicplayer.ui.theme.White
import ghazimoradi.soheil.musicplayer.ui.theme.WhiteAlpha20
import ghazimoradi.soheil.musicplayer.viewmodel.FavoriteViewModel
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayerScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    songList: List<Song>,
    initialIndex: Int = 0,
    padding: PaddingValues,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    var currentIndex by rememberSaveable { mutableIntStateOf(initialIndex) }

    var isShuffle by rememberSaveable { mutableStateOf(false) }
    var isRepeat by rememberSaveable { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    var isFavorite by remember { mutableStateOf(false) }

    var elapsed by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    var shuffledList by remember { mutableStateOf(songList) }

    val amplituda = remember { Amplituda(context) }
    var waveform by remember { mutableStateOf(IntArray(0)) }
    var waveformProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(currentIndex, isShuffle) {
        val list = if (isShuffle) shuffledList else songList
        val song = list.getOrNull(currentIndex) ?: return@LaunchedEffect

        elapsed = 0L
        duration = 0L
        waveformProgress = 0f
        waveform = IntArray(0)

        amplituda.processAudio(
            song.data,
            Cache.withParams(Cache.REUSE)
        ).get(
            { result ->
                val amplitudes = result.amplitudesAsList().map { it.toInt() }.toIntArray()
                Log.d("PlayerScreen", "Extracted ${amplitudes.size} amplitudes")
                waveform = amplitudes
            }, { e ->
                Log.e("PlayerScreen", "Amplituda error: ${e.message}", e)
            }
        )

        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(song.data))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    duration = maxOf(0L, exoPlayer.duration)
                }

                if (playbackState == Player.STATE_ENDED) {
                    currentIndex =
                        (currentIndex + 1) % (if (isShuffle) shuffledList.size else songList.size)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            elapsed = exoPlayer.currentPosition
            waveformProgress = if (duration > 0) elapsed.toFloat() / duration else 0f
            delay(500.milliseconds)
        }
    }

    val song = (if (isShuffle) shuffledList else songList).getOrNull(currentIndex)

    LaunchedEffect(Unit) {
        song?.let { song ->
            favoriteViewModel.isExists(song.id).collect {
                isFavorite = it
            }
        }
    }

    val backgroundBrush = remember {
        Brush.verticalGradient(
            listOf(
                FrostBlack,
                Charade,
            )
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        val maxHeight = maxHeight
        val albumArtSize = (maxHeight * 0.4f).coerceIn(200.dp, 320.dp)

        song?.let {
            val albumUri = remember(it.albumId) {
                ContentUris.withAppendedId(
                    "content://media/external/audio/albumart".toUri(),
                    it.albumId
                )
            }

            val blurImageRequest = remember(albumUri) {
                ImageRequest.Builder(context)
                    .data(albumUri)
                    .build()
            }

            AsyncImage(
                error = painterResource(R.drawable.music_note),
                placeholder = painterResource(R.drawable.music_note),
                alpha = 0.4f,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(18.dp),
                contentDescription = null,
                model = blurImageRequest
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .background(WhiteAlpha20, shape = CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = White
                        )
                    }

                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                favoriteViewModel.delete(it)
                            } else {
                                favoriteViewModel.save(it)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(WhiteAlpha20, shape = CircleShape)
                    ) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Vermillion else White
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                // Album Art
                AsyncImage(
                    placeholder = painterResource(R.drawable.music_note),
                    error = painterResource(R.drawable.music_note),
                    modifier = Modifier
                        .size(albumArtSize)
                        .clip(CircleShape)
                        .background(WhiteAlpha20, shape = CircleShape),
                    contentDescription = null,
                    model = albumUri
                )

                Spacer(Modifier.weight(1f))

                // Song Info
                Text(
                    it.title.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    it.artist.orEmpty(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(24.dp))

                // Progress Info
                PlaybackProgress(
                    elapsed = elapsed,
                    duration = duration,
                    waveform = waveform,
                    waveformProgress = waveformProgress,
                    onSeek = { percent ->
                        val seek = (percent * duration).toLong()
                        exoPlayer.seekTo(seek)
                        elapsed = seek
                        waveformProgress = percent
                    }
                )

                Spacer(Modifier.weight(1f))

                // Controls
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isRepeat = !isRepeat
                            exoPlayer.repeatMode =
                                if (isRepeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                        }
                    ) {
                        Icon(
                            Icons.Outlined.RepeatOne,
                            contentDescription = null,
                            tint = if (isRepeat) Bayside else White
                        )
                    }

                    IconButton(
                        onClick = {
                            val list = if (isShuffle) shuffledList else songList
                            currentIndex =
                                if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
                        }
                    ) {
                        Icon(
                            Icons.Outlined.SkipPrevious,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .size(64.dp)
                            .background(White, shape = CircleShape),
                        onClick = {
                            if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                        }
                    ) {
                        Icon(
                            if (isPlaying) Icons.Outlined.Pause else Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = EerieBlack,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val list = if (isShuffle) shuffledList else songList
                            currentIndex = (currentIndex + 1) % list.size
                        }
                    ) {
                        Icon(
                            Icons.Outlined.SkipNext,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            isShuffle = !isShuffle
                            shuffledList = if (isShuffle) songList.shuffled() else songList
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Shuffle,
                            contentDescription = null,
                            tint = if (isShuffle) Bayside else White
                        )
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val totalSeconds = maxOf(0, seconds)
    val minutes = totalSeconds / 60
    val secs = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, secs)
}

@Composable
fun PlaybackProgress(
    elapsed: Long,
    duration: Long,
    waveform: IntArray,
    waveformProgress: Float,
    onSeek: (Float) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatTime((elapsed / 1000).toInt()),
            color = White,
            fontSize = 13.sp
        )

        Text(
            text = formatTime((duration / 1000).toInt()),
            color = White,
            fontSize = 13.sp
        )
    }

    WaveformBar(
        values = waveform,
        process = waveformProgress,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        onSeek = onSeek
    )
}