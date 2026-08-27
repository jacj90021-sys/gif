package com.jacj90021.gifanywhere.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.ui.theme.*
import kotlinx.coroutines.launch

/* ---------- Header ---------- */

@Composable
fun H1(text: String, size: TextUnit = 30.sp, topPad: Dp = 14.dp, hPad: Dp = 20.dp, bottomPad: Dp = 0.dp) {
    Text(
        text = text + ".",
        fontFamily = Lilita,
        fontSize = size,
        letterSpacing = 0.3.sp,
        color = OffWhite,
        modifier = Modifier.padding(start = hPad, end = hPad, top = topPad, bottom = bottomPad)
    )
}

@Composable
fun MonoLabel(text: String, modifier: Modifier = Modifier, hPad: Dp = 20.dp, topPad: Dp = 20.dp, bottomPad: Dp = 8.dp) {
    Text(
        text = text.uppercase(),
        fontFamily = Mono,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = OffFaint,
        letterSpacing = 1.sp,
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = hPad, end = hPad, top = topPad)
            .clip(RoundedCornerShape(16.dp))
            .background(Charcoal)
            .border(2.dp, LineColor, RoundedCornerShape(16.dp))
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Text("⌕", color = Yellow, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        Spacer(Modifier.width(10.dp))
        if (onValueChange != null && value != null) {
            Box(Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        hint,
                        color = OffFaint,
                        fontSize = 13.5.sp,
                        fontFamily = InterTight,
                        maxLines = 1
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = InterTight, fontSize = 13.5.sp, color = OffWhite),
                    cursorBrush = SolidColor(Yellow),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text(hint, color = OffDim, fontSize = 13.5.sp, fontFamily = InterTight, modifier = Modifier.weight(1f))
        }
    }
}

/* ---------- Segmented control ---------- */

@Composable
fun Segment(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 14.dp) {
    Row(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = topPad)
            .clip(RoundedCornerShape(14.dp))
            .background(Charcoal)
            .padding(4.dp)
    ) {
        options.forEachIndexed { i, opt ->
            Text(
                text = opt,
                fontFamily = InterTight,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = if (i == selected) InkBlack else OffDim,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (i == selected) Yellow else Color.Transparent)
                    .clickableNoRipple { onSelect(i) }
                    .padding(vertical = 9.dp)
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (on) Yellow else Charcoal)
                        .border(2.dp, if (on) Yellow else LineColor, RoundedCornerShape(20.dp))
                        .clickableNoRipple { onSelect(i) }
                        .padding(horizontal = 15.dp, vertical = 9.dp)
                ) {
                    Text(
                        options[i],
                        fontFamily = InterTight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (on) InkBlack else OffWhite
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
        Text(label, fontFamily = InterTight, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OffDim)
        Text(valueText, fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = Yellow)
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
                .height(5.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(Charcoal2)
        )
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(5.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(Yellow)
        )
        val density = LocalDensity.current
        val handleX = with(density) { (width.toPx() * progress.coerceIn(0f, 1f)).toDp() } - 8.dp
        Box(
            Modifier
                .offset(x = handleX)
                .align(Alignment.CenterStart)
                .size(16.dp)
                .clip(CircleShape)
                .background(Yellow)
                .border(3.dp, Charcoal, CircleShape)
        )
        Box(
            Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        onProgress((down.position.x / size.width).coerceIn(0f, 1f))
                        horizontalDrag(down.id) { change ->
                            onProgress((change.position.x / size.width).coerceIn(0f, 1f))
                        }
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
                .height(5.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(Charcoal2)
        )
        Box(
            Modifier
                .offset(x = width * start.coerceIn(0f, 1f))
                .width(width * (end - start).coerceIn(0f, 1f))
                .height(5.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(Yellow)
        )
        val density = LocalDensity.current
        val sx = with(density) { (width.toPx() * start).toDp() } - 8.dp
        val ex = with(density) { (width.toPx() * end).toDp() } - 8.dp
        Box(
            Modifier
                .offset(x = sx)
                .align(Alignment.CenterStart)
                .size(16.dp)
                .clip(CircleShape)
                .background(Yellow)
                .border(3.dp, Charcoal, CircleShape)
        )
        Box(
            Modifier
                .offset(x = ex)
                .align(Alignment.CenterStart)
                .size(16.dp)
                .clip(CircleShape)
                .background(Yellow)
                .border(3.dp, Charcoal, CircleShape)
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
        targetValue = if (checked) 19.dp else 2.dp,
        animationSpec = tween(150),
        label = "toggleKnob"
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(width = 42.dp, height = 25.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(if (checked) Yellow else Charcoal2)
            .border(2.dp, if (checked) Yellow else LineColor, RoundedCornerShape(13.dp))
            .clickableNoRipple { onChange(!checked) }
    ) {
        Box(
            Modifier
                .offset(x = knobOffset)
                .size(17.dp)
                .clip(CircleShape)
                .background(if (checked) InkBlack else OffFaint)
        )
    }
}

/* ---------- Status pill ---------- */

@Composable
fun StatusPill(active: Boolean, text: String, onClick: (() -> Unit)? = null) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Yellow else Charcoal2)
            .then(if (active) Modifier else Modifier.border(1.dp, LineColor, RoundedCornerShape(20.dp)))
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(
            text.uppercase(),
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
            color = if (active) InkBlack else OffFaint
        )
    }
}

/* ---------- Settings group / rows ---------- */

@Composable
fun SettingsCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Charcoal)
            .border(2.dp, LineColor, RoundedCornerShape(18.dp)),
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
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(LineColor)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = OffWhite)
            if (sub != null) {
                Text(sub, fontFamily = InterTight, fontSize = 11.sp, color = OffFaint, modifier = Modifier.padding(top = 2.dp))
            }
        }
        if (value != null) {
            Text(value, fontFamily = Mono, fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp, color = OffDim)
        }
        if (trailing != null) trailing()
        if (chevron) {
            Text("›", color = OffFaint, fontSize = 18.sp, fontFamily = InterTight, modifier = Modifier.padding(start = 6.dp))
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
    val shown = when (state) {
        0 -> label
        1 -> "EXPORTING..."
        else -> "✓ SAVED TO LIBRARY"
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Yellow)
            .alpha(if (state == 1) 0.7f else 1f)
            .clickableNoRipple(enabled = state == 0) {
                state = 1
                scope.launch {
                    kotlinx.coroutines.delay(1200)
                    onExport()
                    toast("Saved to Library")
                    state = 2
                    kotlinx.coroutines.delay(1400)
                    state = 0
                }
            }
            .padding(vertical = 16.dp)
    ) {
        Text(shown, fontFamily = Lilita, fontSize = 16.sp, letterSpacing = 0.3.sp, color = InkBlack)
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
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Yellow)
            .clickableNoRipple { onClick() }
            .padding(vertical = 16.dp)
    ) {
        Text(label, fontFamily = Lilita, fontSize = 16.sp, letterSpacing = 0.3.sp, color = InkBlack)
    }
}

/* ---------- helpers ---------- */

fun Modifier.clickableNoRipple(enabled: Boolean = true, onClick: () -> Unit): Modifier =
    this.then(
        Modifier.pointerInput(enabled) {
            if (enabled) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    val up = waitForUpOrCancellation()
                    if (up != null) onClick()
                }
            }
        }
    )
