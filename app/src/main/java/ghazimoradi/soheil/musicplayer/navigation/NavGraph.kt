package ghazimoradi.soheil.musicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ghazimoradi.soheil.musicplayer.navigation.Screens.SongList
import ghazimoradi.soheil.musicplayer.navigation.Screens.Splash
import ghazimoradi.soheil.musicplayer.screens.SongListScreen
import ghazimoradi.soheil.musicplayer.screens.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Splash.route
    ) {
        composable(route = Splash.route) {
            SplashScreen(
                navigateToSongList = {
                    navController.navigate(SongList.route)
                },
                padding = padding,
            )
        }

        composable(route = SongList.route) {
            SongListScreen(padding) { s, l -> }
        }
    }
}