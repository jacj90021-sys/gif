package com.jacj90021.gifanywhere.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Single source of truth for app state. Primitives persist to SharedPreferences;
 * lists are session state seeded on first run.
 */
object Store {
    // Export panel (Studio)
    var exportFormat by mutableStateOf("GIF")
    var platform by mutableStateOf<String?>(null)
    var colors by mutableStateOf(256f)
    var fps by mutableStateOf(24f)
    var targetMB by mutableStateOf(8f)
    var batch by mutableStateOf(false)

    // Settings
    var onboardingDone by mutableStateOf(false)
    var bubbleEnabled by mutableStateOf(false)
    var bubbleOpacity by mutableStateOf(90)      // percent
    var bubbleSide by mutableStateOf("Right")    // Left / Right
    var bubbleY by mutableStateOf(0.8f)          // vertical fraction of screen
    var defaultFormat by mutableStateOf("Automatic")
    var quality by mutableStateOf("Auto")
    var batteryBehavior by mutableStateOf("Balanced")
    var theme by mutableStateOf("Dark")
    var language by mutableStateOf("English")

    // Library
    var wallTarget by mutableStateOf("Home")     // Home / Lock / Both
    var cacheMB by mutableStateOf(380f)
    var favIds = mutableStateListOf<Int>()
    val folders = mutableStateListOf(
        Folder("Reactions", 42, 0),
        Folder("Work Chat", 18, 1),
        Folder("Sticker Packs", 7, 2)
    )
    val recent = mutableStateListOf(
        RecentItem("Drake yes no", 0),
        RecentItem("Victory dance", 3),
        RecentItem("Heart burst", 2),
        RecentItem("Typing cat", 4)
    )
    val creations = mutableStateListOf<Creation>()

    private val prefsName = "gif_anywhere"

    fun load(ctx: Context) {
        val p = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        exportFormat = p.getString("exportFormat", "GIF") ?: "GIF"
        colors = p.getFloat("colors", 256f)
        fps = p.getFloat("fps", 24f)
        targetMB = p.getFloat("targetMB", 8f)
        batch = p.getBoolean("batch", false)
        onboardingDone = p.getBoolean("onboardingDone", false)
        bubbleEnabled = p.getBoolean("bubbleEnabled", false)
        bubbleOpacity = p.getInt("bubbleOpacity", 90)
        bubbleSide = p.getString("bubbleSide", "Right") ?: "Right"
        bubbleY = p.getFloat("bubbleY", 0.8f)
        defaultFormat = p.getString("defaultFormat", "Automatic") ?: "Automatic"
        quality = p.getString("quality", "Auto") ?: "Auto"
        batteryBehavior = p.getString("batteryBehavior", "Balanced") ?: "Balanced"
        theme = p.getString("theme", "Dark") ?: "Dark"
        language = p.getString("language", "English") ?: "English"
        wallTarget = p.getString("wallTarget", "Home") ?: "Home"
        cacheMB = p.getFloat("cacheMB", 380f)
        favIds.clear()
        favIds.addAll(p.getStringSet("favIds", emptySet())?.mapNotNull { it.toIntOrNull() } ?: emptyList())
    }

    fun save(ctx: Context) {
        val e = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit()
        e.putString("exportFormat", exportFormat)
        e.putFloat("colors", colors)
        e.putFloat("fps", fps)
        e.putFloat("targetMB", targetMB)
        e.putBoolean("batch", batch)
        e.putBoolean("onboardingDone", onboardingDone)
        e.putBoolean("bubbleEnabled", bubbleEnabled)
        e.putInt("bubbleOpacity", bubbleOpacity)
        e.putString("bubbleSide", bubbleSide)
        e.putFloat("bubbleY", bubbleY)
        e.putString("defaultFormat", defaultFormat)
        e.putString("quality", quality)
        e.putString("batteryBehavior", batteryBehavior)
        e.putString("theme", theme)
        e.putString("language", language)
        e.putString("wallTarget", wallTarget)
        e.putFloat("cacheMB", cacheMB)
        e.putStringSet("favIds", favIds.map { it.toString() }.toSet())
        e.apply()
    }

    fun toggleFavorite(id: Int) {
        if (favIds.contains(id)) favIds.remove(id) else favIds.add(id)
    }
}
