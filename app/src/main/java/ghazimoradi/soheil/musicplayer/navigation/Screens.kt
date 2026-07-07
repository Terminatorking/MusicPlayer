package ghazimoradi.soheil.musicplayer.navigation

sealed class Screens(val route: String) {
    object Splash: Screens(route = "Splash")
    object SongList: Screens(route = "SongList")
    object Player: Screens(route = "Player")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}