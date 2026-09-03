package com.jacj90021.gifanywhere.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.R

private val JetBrainsMono = FontFamily(
    // Static weight files — real glyphs at each weight, no faux-bold.
    Font(R.font.jetbrains_mono_500, FontWeight.Medium),
    Font(R.font.jetbrains_mono_600, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono_700, FontWeight.Bold),
    Font(R.font.jetbrains_mono_700, FontWeight.ExtraBold),
)

private val PlusJakartaSans = FontFamily(
    // Static weight files — real glyphs at each weight, no faux-bold.
    Font(R.font.pjs_regular, FontWeight.Normal),
    Font(R.font.pjs_medium, FontWeight.Medium),
    Font(R.font.pjs_semibold, FontWeight.SemiBold),
    Font(R.font.pjs_bold, FontWeight.Bold),
    Font(R.font.pjs_extrabold, FontWeight.ExtraBold),
)

// ---- Sans styles (Plus Jakarta Sans) ----

/** 24px h1 — DISCOVER / STUDIO / LIBRARY / SETTINGS. */
val Headline = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 24.sp,
    fontWeight = FontWeight.ExtraBold,
    letterSpacing = (-0.5).sp,
)

/** 18px section header — WALLPAPERS. */
val HeadlineSm = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 18.sp,
    fontWeight = FontWeight.ExtraBold,
    letterSpacing = (-0.5).sp,
)

/** 12px 800 — tool names, folder names, setting row titles, cache title. */
val Title = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 12.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

/** 10px regular — tool subtitles and setting row subtitles. */
val Sub = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 10.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 10px 700 — chips, platform chips, source labels, clear-cache. */
val Bold = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 10.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 11px 800 — segmented control options, format chips. */
val Extra = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 11.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)

/** 11px 700 — slider label. */
val Slider = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)

/** 13px 800 uppercase — primary buttons (EXPORT GIF →). */
val Button = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 13.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 18.sp,
    letterSpacing = 0.5.sp,
)

/** 10px 800 — action-sheet labels. */
val Sheet = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 10.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 12px 700 — search field text. */
val Search = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

/** 9px 800 — wallpaper SET tag. */
val Wall = TextStyle(
    fontFamily = PlusJakartaSans,
    fontSize = 9.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 13.sp,
    letterSpacing = 0.sp,
)

// ---- Mono styles (JetBrains Mono) ----

/** 9px 800 — BETA tag, folder counts. */
val MonoTag = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 9.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 12.sp,
    letterSpacing = 0.sp,
)

/** 10px 800 — ↻ LOOP badge and feature badges. */
val MonoBadge = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 10.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 8.5px 700 — bottom nav item labels. */
val MonoNav = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 8.5.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 12.sp,
    letterSpacing = 0.sp,
)

/** 9.5px 800 uppercase w/ tracking — card labels (EXPORT FORMAT). */
val MonoLabel = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 9.5.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 13.sp,
    letterSpacing = 0.5.sp,
)

/** 10px 800 — slider value. */
val MonoVal = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 10.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 10px 700 — setting value text. */
val MonoValMuted = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 10.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 14.sp,
    letterSpacing = 0.sp,
)

/** 11px 800 — cache size. */
val MonoSize = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 11.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)

/** 8.5px 800 — status pills. */
val MonoPill = TextStyle(
    fontFamily = JetBrainsMono,
    fontSize = 8.5.sp,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 12.sp,
    letterSpacing = 0.sp,
)

// Material defaults so anything unstyled still fits the app.
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = Search,
    titleMedium = Title,
    labelLarge = Button,
    labelMedium = Extra,
    bodySmall = Sub,
    labelSmall = MonoTag,
)
