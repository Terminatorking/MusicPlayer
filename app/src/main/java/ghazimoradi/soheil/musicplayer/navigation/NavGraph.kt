package ghazimoradi.soheil.musicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ghazimoradi.soheil.musicplayer.screens.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(route = Screens.Splash.route) {
            SplashScreen(
                navigate = {},
                padding = padding,
            )
        }
    }
}