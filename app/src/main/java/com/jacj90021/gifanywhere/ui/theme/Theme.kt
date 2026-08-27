package com.jacj90021.gifanywhere.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.R

// Exact palette from the reference CSS
val Yellow = Color(0xFFFFD400)
val YellowDeep = Color(0xFFE6BE00)
val InkBlack = Color(0xFF0A0A0A)
val Charcoal = Color(0xFF161615)
val Charcoal2 = Color(0xFF1F1F1E)
val LineColor = Color(0xFF2B2B29)
val OffWhite = Color(0xFFFAFAF5)
val OffDim = Color(0xFFB8B6AC)
val OffFaint = Color(0xFF6D6B62)
val RecRed = Color(0xFFFF3B3B)

val Lilita = FontFamily(Font(R.font.lilita_one, FontWeight.Normal))

val InterTight = FontFamily(
    Font(R.font.inter_400, FontWeight.Normal),
    Font(R.font.inter_500, FontWeight.Medium),
    Font(R.font.inter_600, FontWeight.SemiBold),
    Font(R.font.inter_700, FontWeight.Bold),
    Font(R.font.inter_800, FontWeight.ExtraBold)
)

val Mono = FontFamily(
    Font(R.font.jetbrains_mono_500, FontWeight.Medium),
    Font(R.font.jetbrains_mono_600, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono_700, FontWeight.Bold)
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(fontFamily = Lilita, fontSize = 30.sp, letterSpacing = 0.3.sp),
    titleLarge = TextStyle(fontFamily = Lilita, fontSize = 20.sp),
    titleMedium = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 13.5.sp),
    titleSmall = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 12.sp),
    bodyMedium = TextStyle(fontFamily = InterTight, fontSize = 13.sp),
    bodySmall = TextStyle(fontFamily = InterTight, fontSize = 11.sp),
    labelSmall = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 10.sp),
    labelMedium = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 11.5.sp),
    labelLarge = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 12.sp)
)

@Composable
fun GifAnywhereTheme(content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        primary = Yellow,
        onPrimary = InkBlack,
        secondary = YellowDeep,
        onSecondary = InkBlack,
        background = InkBlack,
        onBackground = OffWhite,
        surface = Charcoal,
        onSurface = OffWhite,
        surfaceVariant = Charcoal2,
        onSurfaceVariant = OffDim,
        outline = LineColor,
        error = RecRed
    )
    MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
}
