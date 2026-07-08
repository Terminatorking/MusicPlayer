package ghazimoradi.soheil.musicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ghazimoradi.soheil.musicplayer.data.model.Song
import ghazimoradi.soheil.musicplayer.navigation.Screens.SongList
import ghazimoradi.soheil.musicplayer.navigation.Screens.Splash
import ghazimoradi.soheil.musicplayer.navigation.Screens.Player
import ghazimoradi.soheil.musicplayer.ui.screens.player.PlayerScreen
import ghazimoradi.soheil.musicplayer.ui.screens.songlist.SongListScreen
import ghazimoradi.soheil.musicplayer.ui.screens.splash.SplashScreen

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
            SplashScreen {
                navController.navigate(SongList.route) {
                    popUpTo(Splash.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = SongList.route) {
            SongListScreen(padding = padding) { songs, position ->
                navController.apply {
                    currentBackStackEntry?.savedStateHandle?.set(
                        "songList",
                        songs
                    )
                    navigate(Player.withArgs(position))
                }
            }
        }

        composable(
            route = Player.route + "/{index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val songs =
                navController.previousBackStackEntry?.
                savedStateHandle?.get<List<Song>>("songList")
                    ?: emptyList()

            val index = backStackEntry.arguments?.getInt("index") ?: 0

            PlayerScreen(
                songList = songs,
                initialIndex = index,
                padding = padding,
            ) {
                navController.popBackStack()
            }
        }
    }
}