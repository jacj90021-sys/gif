package com.jacj90021.gifanywhere.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.ui.theme.*
import kotlinx.coroutines.launch



/* ---------- Neo-brutalist primitives ---------- */

/** Hard offset shadow — the signature of the neo-brutalist look. */
fun Modifier.hardShadow(depth: Dp = 3.dp, color: Color = InkBlack): Modifier = this.then(
    Modifier.drawBehind {
        if (depth.value > 0f) {
            drawRoundRect(
                color = color,
                topLeft = Offset(depth.toPx(), depth.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
        }
    }
)

/** White card: 2px ink border + hard shadow + 12dp corners. */
@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    corner: Dp = 12.dp,
    shadow: Dp = 3.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(corner))
            .background(CardWhite)
            .hardShadow(shadow)
            .border(2.dp, InkBlack, RoundedCornerShape(corner)),
        content = content
    )
}

/** Dashed divider used inside white cards (matches the reference CSS). */
@Composable
fun DashedDivider(color: Color = InkBlack.copy(alpha = 0.12f)) {
    val strokeColor = color
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.5.dp)
            .drawBehind {
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(8.dp.toPx(), 6.dp.toPx())
                    )
                )
            }
    )
}

/* ---------- Header ---------- */

@Composable
fun H1(
    text: String,
    size: TextUnit = 26.sp,
    topPad: Dp = 14.dp,
    hPad: Dp = 20.dp,
    bottomPad: Dp = 0.dp,
    eyebrow: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = hPad, end = hPad, top = topPad, bottom = bottomPad)
    ) {
        Text(
            text = text.uppercase() + ".",
            fontFamily = Lilita,
            fontSize = size,
            letterSpacing = (-0.5).sp,
            color = InkBlack
        )
        if (eyebrow != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                eyebrow.uppercase(),
                fontFamily = Mono,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 9.sp,
                letterSpacing = 1.5.sp,
                color = InkBlack,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(InkBlack)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
    }
}

/**
 * Press feedback: spring scale-down while pressed, full accessibility
 * semantics, no ripple (the dark design language uses scale, not ripple).
 */
@Composable
fun Modifier.pressable(enabled: Boolean = true, pressedScale: Float = 0.97f, onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pressScale"
    )
    return this
        .scale(scale)
        .clickable(interactionSource = source, indication = null, enabled = enabled, onClick = onClick)
}

@Composable
fun MonoLabel(text: String, modifier: Modifier = Modifier, hPad: Dp = 20.dp, topPad: Dp = 20.dp, bottomPad: Dp = 8.dp) {
    Text(
        text = text.uppercase(),
        fontFamily = Mono,
        fontSize = 9.5.sp,
        fontWeight = FontWeight.ExtraBold,
        color = InkMuted,
        letterSpacing = 0.5.sp,
        modifier = modifier.padding(start = hPad, end = hPad, top = topPad, bottom = bottomPad)
    )
}

/* ---------- Search bar ---------- */

@Composable
fun SearchBar(
    hint: String,
    value: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    topPad: Dp = 14.dp,
    hPad: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    val focusSource = remember { MutableInteractionSource() }
    val focused by focusSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(if (focused) InkBlack else InkBlack, tween(200), label = "searchBorder")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = hPad, end = hPad, top = topPad)
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 12.dp, vertical = 11.dp)
    ) {
        Text("⌕", color = InkBlack, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        Spacer(Modifier.width(8.dp))
        if (onValueChange != null && value != null) {
            Box(Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        hint,
                        color = InkMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterTight,
                        maxLines = 1
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = InkBlack),
                    cursorBrush = SolidColor(InkBlack),
                    interactionSource = focusSource,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text(
                hint,
                color = InkMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterTight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/* ---------- Segmented control ---------- */

@Composable
fun Segment(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 14.dp) {
    Row(
        modifier = modifier
            .padding(start = 18.dp, end = 18.dp, top = topPad)
            .clip(RoundedCornerShape(12.dp))
            .background(InkBlack.copy(alpha = 0.06f))
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .padding(3.dp)
    ) {
        options.forEachIndexed { i, opt ->
            val on = i == selected
            val bg by animateColorAsState(if (on) CardWhite else Color.Transparent, tween(180), label = "segBg")
            val fg by animateColorAsState(if (on) InkBlack else InkMuted, tween(180), label = "segFg")
            Text(
                text = opt.uppercase(),
                fontFamily = InterTight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = fg,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bg)
                    .then(if (on) Modifier.hardShadow(2.dp) else Modifier)
                    .then(if (on) Modifier.border(2.dp, InkBlack, RoundedCornerShape(8.dp)) else Modifier)
                    .pressable { onSelect(i) }
                    .padding(vertical = 7.dp)
            )
        }
    }
}

/* ---------- Chip row ---------- */

@Composable
fun ChipRow(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 14.dp) {
    Box(modifier = modifier.fillMaxWidth().padding(top = topPad)) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(options.size) { i ->
                val on = i == selected
                val bg by animateColorAsState(if (on) InkBlack else CardWhite, tween(180), label = "chipBg")
                val fg by animateColorAsState(if (on) CardWhite else InkBlack, tween(180), label = "chipFg")
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bg)
                        .border(2.dp, InkBlack, RoundedCornerShape(20.dp))
                        .pressable { onSelect(i) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        options[i],
                        fontFamily = InterTight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = fg
                    )
                }
            }
        }
        // trailing fade signals more chips off-screen
        EdgeFade()
    }
}

/**
 * Gradient veil over the trailing (or leading) edge of a horizontally
 * scrolling row so cut-off content clearly reads as scrollable.
 * Must be inside a Box scope; pass the row's background color.
 */
@Composable
fun BoxScope.EdgeFade(color: Color = InkBlack, width: Dp = 32.dp, start: Boolean = false) {
    Box(
        Modifier
            .align(if (start) Alignment.CenterStart else Alignment.CenterEnd)
            .width(width)
            .fillMaxHeight()
            .background(
                Brush.horizontalGradient(
                    colors = if (start) listOf(color, color.copy(alpha = 0f))
                    else listOf(color.copy(alpha = 0f), color)
                )
            )
    )
}

/* ---------- Slider (single value) ---------- */

@Composable
fun SliderRow(label: String, valueText: String, topPad: Dp = 12.dp) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPad)
    ) {
        Text(label, fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = InkBlack)
        Text(
            valueText,
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 10.sp,
            color = InkBlack,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(BgYellow)
                .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 1.dp)
        )
    }
}

@Composable
fun GifSlider(progress: Float, onProgress: (Float) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 8.dp) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPad)
            .height(20.dp)
    ) {
        val width = maxWidth
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(InkBlack.copy(alpha = 0.08f))
                .border(2.dp, InkBlack, RoundedCornerShape(3.dp))
        )
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(6.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(BgYellow)
        )
        val density = LocalDensity.current
        val handleX = with(density) { (width.toPx() * progress.coerceIn(0f, 1f)).toDp() } - 7.dp
        var dragging by remember { mutableStateOf(false) }
        val handleScale by animateFloatAsState(
            targetValue = if (dragging) 1.35f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            label = "handleScale"
        )
        Box(
            Modifier
                .offset(x = handleX)
                .align(Alignment.CenterStart)
                .scale(handleScale)
                .size(14.dp)
                .clip(CircleShape)
                .background(CardWhite)
                .border(2.dp, InkBlack, CircleShape)
        )
        Box(
            Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        dragging = true
                        onProgress((down.position.x / size.width).coerceIn(0f, 1f))
                        horizontalDrag(down.id) { change ->
                            onProgress((change.position.x / size.width).coerceIn(0f, 1f))
                        }
                        dragging = false
                    }
                }
        )
    }
}

/* ---------- Trim slider (range) ---------- */

@Composable
fun TrimSlider(start: Float, end: Float, onChange: (Float, Float) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 8.dp) {
    val currentStart = rememberUpdatedState(start)
    val currentEnd = rememberUpdatedState(end)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPad)
            .height(24.dp)
    ) {
        val width = maxWidth
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(InkBlack.copy(alpha = 0.08f))
                .border(2.dp, InkBlack, RoundedCornerShape(3.dp))
        )
        Box(
            Modifier
                .offset(x = width * start.coerceIn(0f, 1f))
                .width(width * (end - start).coerceIn(0f, 1f))
                .height(6.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(BgYellow)
        )
        val density = LocalDensity.current
        val sx = with(density) { (width.toPx() * start).toDp() } - 7.dp
        val ex = with(density) { (width.toPx() * end).toDp() } - 7.dp
        Box(
            Modifier
                .offset(x = sx)
                .align(Alignment.CenterStart)
                .size(14.dp)
                .clip(CircleShape)
                .background(CardWhite)
                .border(2.dp, InkBlack, CircleShape)
        )
        Box(
            Modifier
                .offset(x = ex)
                .align(Alignment.CenterStart)
                .size(14.dp)
                .clip(CircleShape)
                .background(CardWhite)
                .border(2.dp, InkBlack, CircleShape)
        )
        Box(
            Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val frac = (down.position.x / size.width).coerceIn(0f, 1f)
                        var which = if (kotlin.math.abs(frac - currentStart.value) <
                            kotlin.math.abs(frac - currentEnd.value)) 0 else 1
                        horizontalDrag(down.id) { change ->
                            val f = (change.position.x / size.width).coerceIn(0f, 1f)
                            if (which == 0) {
                                val s = f.coerceAtMost(currentEnd.value - 0.05f).coerceAtLeast(0f)
                                onChange(s, currentEnd.value)
                            } else {
                                val e = f.coerceAtLeast(currentStart.value + 0.05f).coerceAtMost(1f)
                                onChange(currentStart.value, e)
                            }
                        }
                    }
                }
        )
    }
}

/* ---------- Toggle ---------- */

@Composable
fun AppToggle(checked: Boolean, onChange: (Boolean) -> Unit) {
    val knobOffset by animateDpAsState(
        targetValue = if (checked) 19.dp else 1.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "toggleKnob"
    )
    val haptics = LocalHapticFeedback.current
    val bg by animateColorAsState(if (checked) BgYellow else Color(0x1A0A0A0A), tween(180), label = "tBg")
    val knob by animateColorAsState(if (checked) InkBlack else InkBlack, tween(180), label = "tKnob")
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(width = 38.dp, height = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(2.dp, InkBlack, RoundedCornerShape(10.dp))
            .clickableNoRipple {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onChange(!checked)
            }
    ) {
        Box(
            Modifier
                .offset(x = knobOffset)
                .size(14.dp)
                .clip(CircleShape)
                .background(knob)
        )
    }
}

/* ---------- Status pill ---------- */

@Composable
fun StatusPill(active: Boolean, text: String, onClick: (() -> Unit)? = null) {
    val bg by animateColorAsState(if (active) BgYellow else CardWhite, tween(200), label = "pillBg")
    val fg by animateColorAsState(if (active) InkBlack else InkMuted, tween(200), label = "pillFg")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.5.dp, InkBlack, RoundedCornerShape(12.dp))
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text.uppercase(),
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 8.5.sp,
            color = fg
        )
    }
}

/* ---------- Settings group / rows ---------- */

@Composable
fun SettingsCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(16.dp)),
        content = content
    )
}

@Composable
fun SettingsRow(
    title: String,
    sub: String? = null,
    value: String? = null,
    chevron: Boolean = false,
    divider: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    if (divider) {
        DashedDivider()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontFamily = InterTight, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = InkBlack)
            if (sub != null) {
                Text(sub, fontFamily = InterTight, fontWeight = FontWeight.Medium, fontSize = 10.sp, color = InkMuted, modifier = Modifier.padding(top = 1.dp))
            }
        }
        if (value != null) {
            Text(value, fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = InkMuted)
        }
        if (trailing != null) trailing()
        if (chevron) {
            Text("›", color = InkMuted, fontSize = 18.sp, fontFamily = InterTight, modifier = Modifier.padding(start = 6.dp))
        }
    }
}

/* ---------- Export button with progress → success transition ---------- */

@Composable
fun ExportAction(
    label: String,
    onExport: () -> Unit,
    modifier: Modifier = Modifier,
    toast: (String) -> Unit = {}
) {
    var state by remember { mutableStateOf(0) } // 0 idle, 1 exporting, 2 done
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val progress = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == 1) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(1200))
        }
    }
    val shown = when (state) {
        0 -> label
        1 -> "EXPORTING..."
        else -> "✓ SAVED TO LIBRARY"
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InkBlack)
            .hardShadow(3.dp, CardWhite)
            .pressable(enabled = state == 0) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                state = 1
                // data change happens IMMEDIATELY so navigating away mid-
                // animation can never silently drop the export
                onExport()
                scope.launch {
                    kotlinx.coroutines.delay(1200)
                    toast("Saved to Library")
                    state = 2
                    kotlinx.coroutines.delay(1400)
                    state = 0
                }
            }
            .padding(vertical = 16.dp)
    ) {
        if (state == 1) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(progress.value)
                    .height(3.dp)
                    .background(InkBlack.copy(alpha = 0.25f))
            )
        }
        Text(shown, fontFamily = Lilita, fontSize = 13.sp, letterSpacing = 0.5.sp, color = BgYellow)
    }
}

/* ---------- Gradient tile ---------- */

@Composable
fun GradientBox(gradIdx: Int, modifier: Modifier = Modifier, corner: Dp = 0.dp, content: @Composable BoxScope.() -> Unit = {}) {
    val g = Content.grads[gradIdx % Content.grads.size]
    Box(
        modifier = modifier
            .then(if (corner > 0.dp) Modifier.clip(RoundedCornerShape(corner)) else Modifier)
            .background(
                Brush.linearGradient(
                    colors = listOf(g.first, g.second),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                )
            ),
        content = content
    )
}

/* ---------- plain yellow action button ---------- */

@Composable
fun YellowButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InkBlack)
            .hardShadow(3.dp, CardWhite)
            .pressable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Text(label, fontFamily = Lilita, fontSize = 13.sp, letterSpacing = 0.5.sp, color = BgYellow)
    }
}

/* ---------- helpers ---------- */

/**
 * Ripple-free clickable that keeps full accessibility semantics
 * (TalkBack focus + click action) — a raw pointerInput gesture detector
 * would be invisible to screen readers.
 */
fun Modifier.clickableNoRipple(enabled: Boolean = true, onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
    )
