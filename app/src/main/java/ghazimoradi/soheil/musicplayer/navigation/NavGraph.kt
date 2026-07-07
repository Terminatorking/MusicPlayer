package ghazimoradi.soheil.musicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ghazimoradi.soheil.musicplayer.data.Song
import ghazimoradi.soheil.musicplayer.navigation.Screens.SongList
import ghazimoradi.soheil.musicplayer.navigation.Screens.Splash
import ghazimoradi.soheil.musicplayer.screens.PlayerScreen
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
            SongListScreen(padding = padding, navController = navController)
        }

        composable(
            route = Screens.Player.route + "/{index}",
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