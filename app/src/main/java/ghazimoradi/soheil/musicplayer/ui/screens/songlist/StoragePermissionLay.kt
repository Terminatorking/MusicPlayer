package ghazimoradi.soheil.musicplayer.ui.screens.songlist

import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ghazimoradi.soheil.musicplayer.ui.theme.White

@Composable
fun StoragePermissionLay() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val context = LocalContext.current

        Text(
            modifier = Modifier.padding(10.dp),
            text = "Storage permission denied grant it in app settings!",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = White
        )

        Button(
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                context.openAppSettings()
            },
        ) {
            Text("Go to settings")
        }

    }
}

fun Context.openAppSettings() {
    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.apply {
        data = fromParts("package", this@openAppSettings.packageName, null)
        addCategory(CATEGORY_DEFAULT)
        addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_NO_HISTORY or FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
    this.startActivity(intent)
}