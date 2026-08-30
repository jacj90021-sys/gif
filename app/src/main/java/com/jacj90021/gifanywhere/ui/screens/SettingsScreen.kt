package com.jacj90021.gifanywhere.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.bubble.BubbleService
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.screens.StatusBarRow
import com.jacj90021.gifanywhere.ui.theme.*

/** Mockup SETTINGS screen: Delivery / Export Defaults / General. */
@Composable
fun SettingsScreen(nav: NavController) {
    val context = LocalContext.current
    var showAbout by remember { mutableStateOf(false) }

    // Keyboard enabled state — refreshed whenever the screen resumes
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    var imeEnabled by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        fun check() {
            imeEnabled = imm.enabledInputMethodList.any { it.packageName == context.packageName }
        }
        check()
        val obs = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) check()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgYellow)
            .verticalScroll(rememberScrollState())
    ) {
        StatusBarRow()
        H1("SETTINGS")

        // ---- Delivery (mockup: Keyboard + Floating Bubble) ----
        MonoLabel("Delivery", topPad = 10.dp)
        SettingsCard {
            SettingsRow(
                title = "Keyboard",
                sub = "System-wide GIF keyboard",
                divider = false,
                trailing = {
                    StatusPill(
                        active = imeEnabled,
                        text = if (imeEnabled) "ACTIVE" else "NOT ENABLED"
                    )
                },
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                }
            )
            SettingsRow(
                title = "Floating Bubble",
                sub = "Quick access overlay",
                trailing = {
                    AppToggle(checked = Store.bubbleEnabled) { v ->
                        if (v && !Settings.canDrawOverlays(context)) {
                            Toast.makeText(context, "Allow 'Display over other apps' first", Toast.LENGTH_LONG).show()
                            context.startActivity(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                            )
                        } else {
                            Store.bubbleEnabled = v
                            if (v) context.startService(Intent(context, BubbleService::class.java))
                            else context.stopService(Intent(context, BubbleService::class.java))
                        }
                    }
                }
            )
        }

        // ---- Export Defaults (mockup: Default Format + Quality) ----
        MonoLabel("Export Defaults", topPad = 8.dp)
        val formats = listOf("Automatic", "GIF", "MP4", "WebP")
        val qualities = listOf("Auto", "High", "Balanced", "Small files")
        SettingsCard {
            SettingsRow(
                title = "Default Format",
                value = Store.defaultFormat,
                chevron = true,
                divider = false,
                onClick = { Store.defaultFormat = formats[(formats.indexOf(Store.defaultFormat) + 1) % formats.size] }
            )
            SettingsRow(
                title = "Quality",
                value = Store.quality,
                chevron = true,
                onClick = { Store.quality = qualities[(qualities.indexOf(Store.quality) + 1) % qualities.size] }
            )
        }

        // ---- General (mockup: Theme Light / Language / About) ----
        MonoLabel("General", topPad = 8.dp)
        val themes = listOf("Light", "Dark")
        val languages = listOf("English", "Español", "Français")
        SettingsCard {
            SettingsRow(
                title = "Theme",
                value = Store.theme,
                chevron = true,
                divider = false,
                onClick = { Store.theme = themes[(themes.indexOf(Store.theme) + 1) % themes.size] }
            )
            SettingsRow(
                title = "Language",
                value = Store.language,
                chevron = true,
                onClick = { Store.language = languages[(languages.indexOf(Store.language) + 1) % languages.size] }
            )
            SettingsRow(
                title = "About / Feedback",
                chevron = true,
                onClick = { showAbout = true }
            )
        }
        Spacer(Modifier.height(24.dp))
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            containerColor = CardWhite,
            titleContentColor = InkBlack,
            textContentColor = InkMuted,
            title = { Text("GIF Anywhere 1.0", fontFamily = Lilita) },
            text = {
                Text(
                    "Create, convert and send looping GIFs from any app.\n\n• Discover — trending GIFs, stickers & memes\n• Studio — creation tools + batch export\n• Library — favorites, wallpapers & storage\n• Keyboard + Floating Bubble for system-wide access\n\nBuilt with Jetpack Compose.",
                    fontFamily = InterTight,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("OK", color = InkBlack, fontFamily = Mono, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
