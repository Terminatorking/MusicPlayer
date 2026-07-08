package ghazimoradi.soheil.musicplayer.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ghazimoradi.soheil.musicplayer.ui.theme.Bayside
import ghazimoradi.soheil.musicplayer.ui.theme.SilverCoin
import ghazimoradi.soheil.musicplayer.ui.theme.White

@Composable
fun ProjectOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String = "Search",
    colors: TextFieldColors = colors(
        focusedLabelColor = Bayside,
        unfocusedLabelColor = SilverCoin,
        cursorColor = Bayside,
        focusedBorderColor = Bayside,
        unfocusedBorderColor = SilverCoin,
        focusedTextColor = White
    ),
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        colors = colors,
        label = {
            Text(label)
        },
        value = value,
        onValueChange = {
            onValueChange.invoke(it)
        }
    )
}