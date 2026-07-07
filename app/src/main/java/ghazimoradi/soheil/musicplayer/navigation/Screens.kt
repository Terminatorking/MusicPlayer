package ghazimoradi.soheil.musicplayer.navigation

sealed class Screens(val route: String) {
    object Splash: Screens(route = "Splash")
    object SongList: Screens(route = "SongList")
}