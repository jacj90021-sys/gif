package com.jacj90021.gifanywhere.ui.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jacj90021.gifanywhere.ui.components.clickableNoRipple
import com.jacj90021.gifanywhere.ui.screens.DiscoverScreen
import com.jacj90021.gifanywhere.ui.screens.LibraryScreen
import com.jacj90021.gifanywhere.ui.screens.SettingsScreen
import com.jacj90021.gifanywhere.ui.screens.StudioScreen
import com.jacj90021.gifanywhere.ui.screens.tools.ToolScreen
import com.jacj90021.gifanywhere.ui.theme.*

private data class Tab(val id: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("discover", "DISCOVER", Icons.Filled.Search),
    Tab("studio", "STUDIO", Icons.Filled.Edit),
    Tab("library", "LIBRARY", Icons.Filled.PhotoLibrary),
    Tab("settings", "SETTINGS", Icons.Filled.Settings)
)

@Composable
fun AppNav(startRoute: String = "discover") {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route ?: startRoute
    val showBar = tabs.any { it.id == route }

    Column(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
    ) {
        NavHost(
            navController = nav,
            startDestination = startRoute.takeIf { tabs.any { t -> t.id == it } } ?: "discover",
            modifier = Modifier.weight(1f)
        ) {
            composable("discover") { DiscoverScreen(nav) }
            composable("studio") { StudioScreen(nav) }
            composable("library") { LibraryScreen(nav) }
            composable("settings") { SettingsScreen(nav) }
            composable("tool/{tool}") { entry ->
                ToolScreen(nav, entry.arguments?.getString("tool") ?: "video")
            }
        }

        // Deep link from the Floating Bubble (e.g. "tool/editor")
        LaunchedEffect(startRoute) {
            if (startRoute.startsWith("tool/")) nav.navigate(startRoute)
        }

        AnimatedVisibility(visible = showBar) {
            BottomBar(route) { id ->
                nav.navigate(id) {
                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}

@Composable
private fun BottomBar(current: String, onSelect: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Charcoal2)
            .border(2.dp, LineColor)
            .padding(vertical = 12.dp, horizontal = 6.dp)
    ) {
        tabs.forEach { tab ->
            val active = tab.id == current
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (active) Yellow else androidx.compose.ui.graphics.Color.Transparent)
                    .clickableNoRipple { onSelect(tab.id) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    tab.icon,
                    contentDescription = tab.label,
                    tint = if (active) InkBlack else OffFaint,
                    modifier = Modifier.size(21.dp)
                )
                Text(
                    tab.label,
                    fontFamily = Mono,
                    fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = 9.5.sp,
                    color = if (active) InkBlack else OffFaint,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
