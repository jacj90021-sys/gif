package com.jacj90021.gifanywhere.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jacj90021.gifanywhere.components.NavBar
import com.jacj90021.gifanywhere.screens.DiscoverScreen
import com.jacj90021.gifanywhere.screens.LibraryScreen
import com.jacj90021.gifanywhere.screens.SettingsScreen
import com.jacj90021.gifanywhere.screens.StudioScreen
import com.jacj90021.gifanywhere.theme.BgYellow

sealed class Screen(
    val route: String,
    val title: String,
) {
    object Discover : Screen("discover", "DISCOVER")
    object Studio : Screen("studio", "STUDIO")
    object Library : Screen("library", "LIBRARY")
    object Settings : Screen("settings", "SETTINGS")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgYellow),
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Discover.route,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            composable(Screen.Discover.route) {
                DiscoverScreen(
                    onNavigateTo = { target -> navController.navigate(target) },
                )
            }
            composable(Screen.Studio.route) {
                StudioScreen(
                    onNavigateTo = { target -> navController.navigate(target) },
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen(
                    onNavigateTo = { target -> navController.navigate(target) },
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateTo = { target -> navController.navigate(target) },
                )
            }
        }

        NavBar(
            activeId = currentDestination?.route ?: Screen.Discover.route,
            onTabSelected = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
    }
}
