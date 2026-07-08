package ghazimoradi.soheil.musicplayer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import ghazimoradi.soheil.musicplayer.ui.theme.White

@Composable
fun WaveformBar(
    modifier: Modifier = Modifier,
    values: IntArray,
    process: Float = 0f,
    activeBarColor: Color = White,
    inactiveBarColor: Color = White,
    onSeek: ((Float) -> Unit)? = null
) {
    val inactiveColor = remember(inactiveBarColor) { inactiveBarColor.copy(alpha = 0.2f) }

    val sampledValues = remember(values) {
        val maxBars = 60
        if (values.isEmpty()) {
            IntArray(0)
        } else if (values.size > maxBars) {
            val step = values.size / maxBars
            IntArray(maxBars) { i -> values[i * step] }
        } else {
            values
        }
    }

    val maxValue = remember(sampledValues) {
        sampledValues.maxOrNull()?.toFloat() ?: 1f
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onSeek?.invoke((offset.x / size.width).coerceIn(0f, 1f))
                }
            }
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (sampledValues.isEmpty()) return@Canvas

            val canvasWidth = size.width
            val canvasHeight = size.height

            val barCount = sampledValues.size
            val spacing = 3.dp.toPx()
            val totalSpacing = spacing * (barCount - 1)
            val barWidth = (canvasWidth - totalSpacing) / barCount

            sampledValues.forEachIndexed { index, value ->
                val x = index * (barWidth + spacing)

                val normalizedHeight = (value.toFloat() / maxValue).coerceAtLeast(0.1f)
                val barHeight = normalizedHeight * canvasHeight

                val color = if (index.toFloat() / barCount < process) activeBarColor else inactiveColor

                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, (canvasHeight - barHeight) / 2),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                )
            }
        }
    }
}