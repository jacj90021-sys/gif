package com.jacj90021.gifanywhere.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Thin-stroke icon set matching the approved reference mockup.
 * Every icon is built from the exact SVG path data used in the design
 * (24×24 viewport, stroke-width 2.2, round caps/joins, no fill).
 */
object AppIcons {

    private fun stroke(name: String, vararg paths: String): ImageVector {
        val builder = ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        )
        val parser = PathParser()
        paths.forEach { p ->
            builder.addPath(
                pathData = parser.parsePathString(p).toNodes(),
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }
        return builder.build()
    }

    // ---- Navigation (mockup bottom bar) ----
    val Discover = stroke(
        "Discover",
        "M21 21l-4.35-4.35",
        "M19 11a8 8 0 11-16 0 8 8 0 0116 0z"
    )
    val Studio = stroke(
        "Studio",
        "M12 20h9",
        "M16.5 3.5a2.1 2.1 0 013 3L7 19l-4 1 1-4z"
    )
    val Library = stroke(
        "Library",
        "M3 6a2 2 0 012-2h14a2 2 0 012 2v12a2 2 0 01-2 2H5a2 2 0 01-2-2z",
        "M8 2v4",
        "M16 2v4",
        "M3 10h18"
    )
    val Settings = stroke(
        "Settings",
        "M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-2 2 2 2 0 01-2-2v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83 0 2 2 0 010-2.83l.06-.06a1.65 1.65 0 00.33-1.82 1.65 1.65 0 00-1.51-1H3a2 2 0 01-2-2 2 2 0 012-2h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 010-2.83 2 2 0 012.83 0l.06.06a1.65 1.65 0 001.82.33H9a1.65 1.65 0 001-1.51V3a2 2 0 012-2 2 2 0 012 2v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 0 2 2 0 010 2.83l-.06.06a1.65 1.65 0 00-.33 1.82V9a1.65 1.65 0 001.51 1H21a2 2 0 012 2 2 2 0 01-2 2h-.09a1.65 1.65 0 00-1.51 1z",
        "M12 15a3 3 0 100-6 3 3 0 000 6z"
    )

    // ---- Action sheet (mockup Discover sheet) ----
    val Send = stroke(
        "Send",
        "M22 2L11 13",
        "M22 2l-7 20-4-9-9-4 20-7z"
    )
    val Bookmark = stroke(
        "Bookmark",
        "M19 21l-7-5-7 5V5a2 2 0 012-2h10a2 2 0 012 2z"
    )
    val Heart = stroke(
        "Heart",
        "M20.8 4.6a5.5 5.5 0 00-7.8 0L12 5.6l-1-1a5.5 5.5 0 00-7.8 7.8l1 1L12 21l7.8-7.8 1-1a5.5 5.5 0 000-7.8z"
    )
    val Repeat = stroke(
        "Repeat",
        "M17 2l4 4-4 4",
        "M3 11V9a4 4 0 014-4h14",
        "M7 22l-4-4 4-4",
        "M21 13v2a4 4 0 01-4 4H3"
    )
    val Image = stroke(
        "Image",
        "M3 3h18v18H3z",
        "M8.5 10a1.5 1.5 0 100-3 1.5 1.5 0 000 3z",
        "M21 15l-5-5L5 21"
    )
    val Camera = stroke(
        "Camera",
        "M23 19a2 2 0 01-2 2H3a2 2 0 01-2-2V8a2 2 0 012-2h4l2-3h6l2 3h4a2 2 0 012 2z",
        "M12 17a4 4 0 100-8 4 4 0 000 8z"
    )
    val Video = stroke(
        "Video",
        "M23 7l-7 5 7 5V7z",
        "M1 7a2 2 0 012-2h11a2 2 0 012 2v10a2 2 0 01-2 2H3a2 2 0 01-2-2z"
    )
    val Link = stroke(
        "Link",
        "M10 13a5 5 0 007.07 0l2.83-2.83a5 5 0 00-7.07-7.07l-1.72 1.71",
        "M14 11a5 5 0 00-7.07 0l-2.83 2.83a5 5 0 007.07 7.07l1.71-1.71"
    )
    val Play = stroke("Play", "M8 5v14l11-7z")
    val Monitor = stroke(
        "Monitor",
        "M2 5a2 2 0 012-2h16a2 2 0 012 2v10a2 2 0 01-2 2H4a2 2 0 01-2-2z",
        "M8 21h8",
        "M12 17v4"
    )
    val Meme = stroke("Meme", "M4 7V4h16v3", "M9 20h6", "M12 4v16")

    // ---- Editor tools (replaces the old emoji set) ----
    val Crop = stroke(
        "Crop",
        "M6.13 1L6 16a2 2 0 002 2h15",
        "M1 6.13L16 6a2 2 0 012 2v15"
    )
    val Maximize = stroke(
        "Maximize",
        "M8 3H5a2 2 0 00-2 2v3",
        "M21 8V5a2 2 0 00-2-2h-3",
        "M3 16v3a2 2 0 002 2h3",
        "M16 21h3a2 2 0 002-2v-3"
    )
    val FastForward = stroke(
        "FastForward",
        "M13 19l9-7-9-7v14z",
        "M2 19l9-7-9-7v14z"
    )
    val RotateCcw = stroke(
        "RotateCcw",
        "M1 4v6h6",
        "M3.51 15a9 9 0 102.13-9.36L1 10"
    )
    val MessageCircle = stroke(
        "MessageCircle",
        "M21 11.5a8.38 8.38 0 01-.9 3.8 8.5 8.5 0 01-7.6 4.7 8.38 8.38 0 01-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 01-.9-3.8 8.5 8.5 0 014.7-7.6 8.38 8.38 0 013.8-.9h.5a8.48 8.48 0 018 8v.5z"
    )
    val Smile = stroke(
        "Smile",
        "M12 22a10 10 0 100-20 10 10 0 000 20z",
        "M8 14s1.5 2 4 2 4-2 4-2",
        "M9 9h.01",
        "M15 9h.01"
    )
    val Sliders = stroke(
        "Sliders",
        "M4 21v-7",
        "M4 10V3",
        "M12 21v-9",
        "M12 8V3",
        "M20 21v-5",
        "M20 12V3",
        "M1 14h6",
        "M9 8h6",
        "M17 16h6"
    )
    val Droplet = stroke("Droplet", "M12 2.69l5.66 5.66a8 8 0 11-11.31 0z")
}
