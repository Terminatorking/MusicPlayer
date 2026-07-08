package ghazimoradi.soheil.musicplayer.ui.screens.songlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ghazimoradi.soheil.musicplayer.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPermissionDialog(
    onGrantClick: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        content = {
            Box(
                modifier = Modifier
                    .background(White, shape = RoundedCornerShape(15.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Storage permission needed ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "For show your songs we need storage permission to granted",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            onGrantClick()
                        },
                    ) {
                        Text("Grant")
                    }
                }
            }
        },
    )
}