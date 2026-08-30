package com.jacj90021.gifanywhere.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

data class GifItem(
    val id: Int,
    val title: String,
    val kind: String,       // GIFs / Stickers / Memes
    val category: String,
    val heightDp: Int,
    val gradIdx: Int
)

data class Folder(val name: String, val count: Int, val gradIdx: Int)
data class RecentItem(val title: String, val gradIdx: Int)
data class Creation(val name: String, val tool: String)

object Content {
    // Neo-brutalist light mockup: preview tiles are pure white (#FFFFFF).
    // Five identical entries keep gradIdx variety semantics without dark tiles.
    val grads: List<Pair<Color, Color>> = listOf(
        Color(0xFFFFFFFF) to Color(0xFFFFFFFF),
        Color(0xFFFFFFFF) to Color(0xFFFFFFFF),
        Color(0xFFFFFFFF) to Color(0xFFFFFFFF),
        Color(0xFFFFFFFF) to Color(0xFFFFFFFF),
        Color(0xFFFFFFFF) to Color(0xFFFFFFFF)
    )

    val kinds = listOf("GIFs", "Stickers", "Memes")
    val categories = listOf("Trending", "Reactions", "Memes", "Anime", "Love", "Sports")

    val gifs = listOf(
        GifItem(1, "Excited nod", "GIFs", "Trending", 190, 0),
        GifItem(2, "Slow clap", "GIFs", "Reactions", 130, 1),
        GifItem(3, "Mind blown", "GIFs", "Trending", 140, 2),
        GifItem(4, "Victory dance", "GIFs", "Sports", 200, 3),
        GifItem(5, "Facepalm loop", "GIFs", "Reactions", 160, 0),
        GifItem(6, "Typing cat", "GIFs", "Anime", 110, 4),
        GifItem(7, "Heart burst", "Stickers", "Love", 150, 2),
        GifItem(8, "Thumbs up pack", "Stickers", "Reactions", 120, 1),
        GifItem(9, "Drake yes no", "Memes", "Memes", 180, 0),
        GifItem(10, "Distracted bf", "Memes", "Memes", 140, 3),
        GifItem(11, "Sweating bullet", "Memes", "Trending", 130, 4),
        GifItem(12, "Confused math", "Memes", "Memes", 170, 1)
    )

    val platforms = listOf("Discord", "Instagram", "WhatsApp")
    val formats = listOf("GIF", "MP4", "WebP", "WebM")
    val sources = listOf("Gallery", "Camera", "Video", "URL")
    val videoEditChips = listOf("Trim", "Crop", "Speed", "Reverse", "Captions", "Stickers", "Filters")
    val mergeLayouts = listOf("Side by side", "Sequence", "Grid")
    val memeFonts = listOf("Impact", "Lilita", "Mono", "Handwritten")
    val editorTools = listOf("TRIM", "CROP", "SPEED", "REVERSE", "CAPTION", "STICKER", "FILTER", "WATERMARK")

    val kbTiles = listOf(
        "Excited nod" to 0, "Slow clap" to 1, "Mind blown" to 2, "Victory dance" to 3,
        "Heart burst" to 2, "Typing cat" to 4, "Facepalm" to 0, "Confused math" to 1
    )

    val wallpaperGrads = listOf(0, 1, 2)
}
