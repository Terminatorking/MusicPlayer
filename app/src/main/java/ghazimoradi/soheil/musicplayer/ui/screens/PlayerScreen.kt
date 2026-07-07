package ghazimoradi.soheil.musicplayer.ui.screens

import android.content.ContentUris
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import ghazimoradi.soheil.musicplayer.R
import ghazimoradi.soheil.musicplayer.data.Song
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayerScreen(
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

    var elapsed by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    var shuffledList by remember { mutableStateOf(songList) }

    val waveform = remember { getWaveForm() }
    var waveformProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(currentIndex, isShuffle) {
        val list = if (isShuffle) shuffledList else songList
        val song = list.getOrNull(currentIndex) ?: return@LaunchedEffect

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
                    duration = exoPlayer.duration
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xff191c1f),
                        Color(0xff2c2c38),
                    )
                )
            )
    ) {
        song?.let {
            val albumUri = ContentUris.withAppendedId(
                "content://media/external/audio/albumart".toUri(),
                it.albumId
            )

            AsyncImage(
                error = painterResource(R.drawable.music_note),
                placeholder = painterResource(R.drawable.music_note),
                alpha = 0.4f,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(18.dp),
                contentDescription = null,
                model = ImageRequest.Builder(context)
                    .data(albumUri)
                    .build()
            )

            Row(
                Modifier.padding(
                    horizontal = 16.dp,
                    vertical = padding.calculateTopPadding() + 30.dp
                )
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = padding.calculateTopPadding() + 100.dp)
            ) {
                AsyncImage(
                    placeholder = painterResource(R.drawable.music_note),
                    error = painterResource(R.drawable.music_note),
                    modifier = Modifier
                        .size(320.dp)
                        .clip(CircleShape)
                        .background(Color(0x30ffffff), shape = CircleShape),

                    contentDescription = null,
                    model = ContentUris.withAppendedId(
                        "content://media/external/audio/albumart".toUri(),
                        it.albumId
                    )
                )

                Text(
                    it.title.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Text(
                    it.artist.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = padding.calculateBottomPadding() + 54.dp),
                horizontalArrangement = Arrangement.Center,
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
                        tint = if (isRepeat) Color(0xff9c27b0) else Color.White
                    )
                }

                IconButton(
                    onClick = {
                        val list = if (isShuffle) shuffledList else songList
                        currentIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
                    }
                ) {
                    Icon(
                        Icons.Outlined.SkipPrevious,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White, shape = CircleShape),
                    onClick = {
                        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                    }
                ) {
                    Icon(
                        if (isPlaying) Icons.Outlined.Pause else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xff1a1a1a)
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
                        tint = Color.White
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
                        tint = if (isShuffle) Color(0xff9c27b0) else Color.White
                    )
                }
            }
        }
    }
}

fun getWaveForm(): IntArray {
    val random = Random(System.currentTimeMillis())
    return IntArray(50) {
        5 + random.nextInt(50)
    }
}