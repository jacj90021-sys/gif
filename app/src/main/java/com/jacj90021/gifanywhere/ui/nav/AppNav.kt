package com.jacj90021.gifanywhere.ui.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jacj90021.gifanywhere.ui.components.AppIcons
import com.jacj90021.gifanywhere.ui.components.hardShadow
import com.jacj90021.gifanywhere.ui.components.pressable
import com.jacj90021.gifanywhere.ui.screens.DiscoverScreen
import com.jacj90021.gifanywhere.ui.screens.LibraryScreen
import com.jacj90021.gifanywhere.ui.screens.SettingsScreen
import com.jacj90021.gifanywhere.ui.screens.StudioScreen
import com.jacj90021.gifanywhere.ui.screens.tools.ToolScreen
import com.jacj90021.gifanywhere.ui.theme.*

private data class Tab(val id: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("discover", "DISCOVER", AppIcons.Discover),
    Tab("studio", "STUDIO", AppIcons.Studio),
    Tab("library", "LIBRARY", AppIcons.Library),
    Tab("settings", "SETTINGS", AppIcons.Settings)
)

@Composable
fun AppNav(routeRequest: String? = null) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route ?: "discover"
    val showBar = tabs.any { it.id == route }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgYellow)
            .statusBarsPadding()   // content starts below the status bar; yellow still renders behind it
    ) {
        NavHost(
            navController = nav,
            startDestination = "discover",
            modifier = Modifier.weight(1f),
            // Motion system: tabs cross-fade with a soft zoom; tool screens
            // slide up like modals and slide back down on close.
            enterTransition = { fadeIn(tween(200)) + scaleIn(initialScale = 0.97f, animationSpec = tween(200)) },
            exitTransition = { fadeOut(tween(140)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(140)) }
        ) {
            composable("discover") { DiscoverScreen(nav) }
            composable("studio") { StudioScreen(nav) }
            composable("library") { LibraryScreen(nav) }
            composable("settings") { SettingsScreen(nav) }
            composable(
                "tool/{tool}",
                enterTransition = {
                    slideInVertically(tween(260)) { it / 6 } + fadeIn(tween(260))
                },
                popExitTransition = {
                    slideOutVertically(tween(220)) { it / 6 } + fadeOut(tween(220))
                }
            ) { entry ->
                ToolScreen(nav, entry.arguments?.getString("tool") ?: "video")
            }
        }

        // Deep link from the Floating Bubble ("studio", "tool/editor", …).
        // Fires on first composition AND whenever a new intent delivers a route.
        LaunchedEffect(routeRequest) {
            val target = routeRequest?.takeIf { it.isNotBlank() && it != "discover" } ?: return@LaunchedEffect
            nav.navigate(target) { launchSingleTop = true }
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
            // background fills edge-to-edge (incl. behind the system nav bar),
            // then the bar content is padded above the system navigation inset
            .background(CardWhite)
            .navigationBarsPadding()
            .border(2.dp, InkBlack)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        tabs.forEach { tab ->
            val active = tab.id == current
            val pillColor by animateColorAsState(
                if (active) BgYellow else androidx.compose.ui.graphics.Color.Transparent,
                tween(200), label = "pill"
            )
            val iconColor by animateColorAsState(
                if (active) InkBlack else InkMuted,
                tween(200), label = "tabIcon"
            )
            val iconScale by animateFloatAsState(
                targetValue = if (active) 1.1f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "tabScale"
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(pillColor)
                    .then(if (active) Modifier.hardShadow(2.dp) else Modifier)
                    .then(if (active) Modifier.border(2.dp, InkBlack, RoundedCornerShape(8.dp)) else Modifier)
                    .pressable { onSelect(tab.id) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    tab.icon,
                    contentDescription = tab.label,
                    tint = iconColor,
                    modifier = Modifier.size(17.dp).scale(iconScale)
                )
                Text(
                    tab.label,
                    fontFamily = Mono,
                    fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = 8.5.sp,
                    color = iconColor,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
