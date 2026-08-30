package com.jacj90021.gifanywhere.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.R

/* ---------- Neo-brutalist palette (from the approved reference) ---------- */
val BgYellow = Color(0xFFFFD600)     // app background — bold yellow
val CardWhite = Color(0xFFFFFFFF)    // card / surface white
val InkBlack = Color(0xFF0A0A0A)     // borders, primary text, shadows
val InkMuted = Color(0xFF555555)     // secondary text on white
val PaperDim = Color(0xFF777777)     // muted text on white (sub rows)
val BlackScrim = Color(0x66000000)   // modal backdrop

// kept for compatibility with older screens / keyboard service
val Yellow = BgYellow
val YellowDeep = Color(0xFFE6BE00)
val Charcoal = CardWhite
val Charcoal2 = CardWhite
val LineColor = InkBlack
val OffWhite = InkBlack
val OffDim = InkMuted
val OffFaint = InkMuted
val RecRed = Color(0xFFFF3B3B)

/* ---------- Type ---------- */
val Lilita = FontFamily(Font(R.font.pjs_extrabold, FontWeight.Normal))

val InterTight = FontFamily(
    Font(R.font.pjs_regular, FontWeight.Normal),
    Font(R.font.pjs_medium, FontWeight.Medium),
    Font(R.font.pjs_semibold, FontWeight.SemiBold),
    Font(R.font.pjs_bold, FontWeight.Bold),
    Font(R.font.pjs_extrabold, FontWeight.ExtraBold)
)

val Mono = FontFamily(
    Font(R.font.jetbrains_mono_500, FontWeight.Medium),
    Font(R.font.jetbrains_mono_600, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono_700, FontWeight.Bold)
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(fontFamily = Lilita, fontSize = 26.sp, letterSpacing = (-0.5).sp),
    titleLarge = TextStyle(fontFamily = Lilita, fontSize = 20.sp),
    titleMedium = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.ExtraBold, fontSize = 13.5.sp),
    titleSmall = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 12.sp),
    bodyMedium = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Medium, fontSize = 13.sp),
    bodySmall = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Medium, fontSize = 11.sp),
    labelSmall = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 9.sp),
    labelMedium = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 10.5.sp),
    labelLarge = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 12.sp)
)

@Composable
fun GifAnywhereTheme(content: @Composable () -> Unit) {
    val scheme = lightColorScheme(
        primary = BgYellow,
        onPrimary = InkBlack,
        secondary = YellowDeep,
        onSecondary = InkBlack,
        background = BgYellow,
        onBackground = InkBlack,
        surface = CardWhite,
        onSurface = InkBlack,
        surfaceVariant = CardWhite,
        onSurfaceVariant = InkMuted,
        outline = InkBlack,
        error = RecRed
    )
    MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
}
