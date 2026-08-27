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
import com.jacj90021.gifanywhere.ui.theme.*

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
            .background(InkBlack)
            .verticalScroll(rememberScrollState())
    ) {
        H1("SETTINGS")

        // ---- Delivery ----
        MonoLabel("Delivery", topPad = 8.dp)
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
            if (Store.bubbleEnabled) {
                SettingsRow(
                    title = "Bubble position",
                    value = "${Store.bubbleSide} · ${(Store.bubbleY * 100).toInt()}%",
                    onClick = {
                        Toast.makeText(context, "Drag the bubble anywhere to reposition", Toast.LENGTH_SHORT).show()
                    }
                )
                SettingsRow(
                    title = "Bubble opacity",
                    value = "${Store.bubbleOpacity}%",
                    onClick = {
                        val options = listOf(100, 90, 80, 70)
                        Store.bubbleOpacity = options[(options.indexOf(Store.bubbleOpacity) + 1) % options.size]
                        // apply live to a running service
                        context.stopService(Intent(context, BubbleService::class.java))
                        context.startService(Intent(context, BubbleService::class.java))
                    }
                )
            }
        }

        // ---- Export defaults ----
        MonoLabel("Export defaults")
        val formats = listOf("Automatic", "GIF", "MP4", "WebP")
        val qualities = listOf("Auto", "High", "Balanced", "Small files")
        val battery = listOf("Balanced", "Saver", "Full FPS")
        SettingsCard {
            SettingsRow(
                title = "Default format",
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
            SettingsRow(
                title = "Wallpaper battery behavior",
                value = Store.batteryBehavior,
                chevron = true,
                onClick = { Store.batteryBehavior = battery[(battery.indexOf(Store.batteryBehavior) + 1) % battery.size] }
            )
        }

        // ---- General ----
        MonoLabel("General")
        val themes = listOf("Dark", "AMOLED")
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
            containerColor = Charcoal,
            titleContentColor = OffWhite,
            textContentColor = OffDim,
            title = { Text("GIF Anywhere 1.0", fontFamily = Lilita) },
            text = {
                Text(
                    "Create, convert and send looping GIFs from any app.\n\n• Discover — trending GIFs, stickers & memes\n• Studio — 7 creation tools + batch export\n• Library — favorites, wallpapers & storage\n• Keyboard + Floating Bubble for system-wide access\n\nBuilt with Jetpack Compose.",
                    fontFamily = InterTight,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("OK", color = Yellow, fontFamily = Mono, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
