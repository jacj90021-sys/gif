package com.jacj90021.gifanywhere.ui.screens.tools

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.data.Creation
import com.jacj90021.gifanywhere.data.RecentItem
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun stamp() = SimpleDateFormat("HHmmss", Locale.US).format(Date())

private fun addCreation(context: Context, name: String, tool: String) {
    Store.creations.add(0, Creation(name, tool))
    Store.recent.add(0, RecentItem(name, (0..4).random()))
    Toast.makeText(context, "Saved to Library", Toast.LENGTH_SHORT).show()
}

@Composable
fun ToolScreen(nav: NavController, tool: String) {
    val context = LocalContext.current
    when (tool) {
        "boomerang" -> BoomerangTool(nav, context)
        "screenrec" -> ScreenRecTool(nav, context)
        "editor" -> EditorTool(nav)
        "meme" -> MemeTool(nav, context)
        "sticker" -> StickerTool(nav, context)
        "merge" -> MergeTool(nav, context)
        else -> VideoToGifTool(nav)
    }
}

@Composable
private fun ToolScaffold(title: String, nav: NavController, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BgYellow)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                "←",
                color = InkBlack,
                fontSize = 20.sp,
                modifier = Modifier.clickableNoRipple { nav.popBackStack() }
            )
            Text(
                buildAnnotatedString {
                    append(title)
                    withStyle(SpanStyle(color = InkBlack)) { append(".") }
                },
                fontFamily = Lilita,
                fontSize = 20.sp,
                letterSpacing = (-0.3).sp,
                color = InkBlack,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        content()
    }
}

/* ================= VIDEO → GIF ================= */

@Composable
private fun VideoToGifTool(nav: NavController) {
    var trimStart by rememberSaveable { mutableStateOf(0.12f) }
    var trimEnd by rememberSaveable { mutableStateOf(0.58f) }
    var chip by rememberSaveable { mutableStateOf(0) }
    ToolScaffold("VIDEO → GIF", nav) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            GradientBox(
                0,
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            )
            MonoLabel("Trim", hPad = 20.dp, topPad = 16.dp, bottomPad = 0.dp)
            TrimSlider(trimStart, trimEnd, { s, e -> trimStart = s; trimEnd = e }, modifier = Modifier.padding(horizontal = 20.dp))
            ChipRow(Content.videoEditChips, chip, { chip = it }, topPad = 18.dp)
        }
        YellowButton("CONTINUE TO EXPORT →") { nav.popBackStack() }
    }
}

/* ================= BOOMERANG ================= */

@Composable
private fun BoomerangTool(nav: NavController, context: Context) {
    var recording by rememberSaveable { mutableStateOf(false) }
    val pulse = rememberInfiniteTransition(label = "recPulse")
    val pulseAlpha by pulse.animateFloat(
        0.65f, 1f,
        infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "recAlpha"
    )
    ToolScaffold("BOOMERANG", nav) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(
                        listOf(CardWhite, CardWhite),
                        start = Offset(0f, 0f),
                        end = Offset(500f, 1200f)
                    )
                )
                .border(2.dp, LineColor, RoundedCornerShape(18.dp))
        ) {
            Text(
                if (recording) "● REC — CAPTURING LOOP" else "LIVE CAMERA PREVIEW",
                fontFamily = Mono,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (recording) RecRed else InkMuted
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardWhite)
                    .border(1.5.dp, InkBlack, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    "↻ BACK & FORTH LOOP",
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = OffWhite
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp, bottom = 34.dp)
        ) {
            Spacer(Modifier.width(26.dp))
            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CardWhite)
                    .border(2.dp, InkBlack, CircleShape)
                    .clickableNoRipple { Toast.makeText(context, "Camera flipped", Toast.LENGTH_SHORT).show() }
            )
            Spacer(Modifier.width(26.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(if (recording) RecRed else BgYellow)
                    .border(4.dp, InkBlack, CircleShape)
                    .then(if (recording) Modifier.alpha(pulseAlpha) else Modifier)
                    .clickableNoRipple {
                        if (!recording) {
                            recording = true
                            Toast.makeText(context, "Recording boomerang…", Toast.LENGTH_SHORT).show()
                        } else {
                            recording = false
                            addCreation(context, "boomerang_${stamp()}.gif", "Boomerang")
                        }
                    }
            ) {
                if (recording) {
                    Box(
                        Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CardWhite)
                    )
                }
            }
            Spacer(Modifier.width(26.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CardWhite)
                    .border(2.dp, InkBlack, CircleShape)
                    .clickableNoRipple { Toast.makeText(context, "Front / back camera", Toast.LENGTH_SHORT).show() }
            ) {
                Text("🔄", fontSize = 16.sp)
            }
            Spacer(Modifier.width(26.dp))
        }
    }
}

/* ================= SCREEN REC ================= */

@Composable
private fun ScreenRecTool(nav: NavController, context: Context) {
    val areas = listOf("Full screen", "Window", "Region")
    val durations = listOf(15, 30, 60)
    var area by rememberSaveable { mutableStateOf(0) }
    var audio by rememberSaveable { mutableStateOf(false) }
    var durIdx by rememberSaveable { mutableStateOf(0) }
    var recording by rememberSaveable { mutableStateOf(false) }
    var elapsed by remember { mutableStateOf(0) }
    val pulse = rememberInfiniteTransition(label = "recPulse")
    val pulseAlpha by pulse.animateFloat(
        0.5f, 1f,
        infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "recAlpha"
    )

    LaunchedEffect(recording, durIdx) {
        if (recording) {
            elapsed = 0
            while (recording && elapsed < durations[durIdx]) {
                delay(1000)
                elapsed++
            }
            if (recording && elapsed >= durations[durIdx]) {
                recording = false
                addCreation(context, "screen_rec_${stamp()}.gif", "Screen Rec")
            }
        }
    }

    ToolScaffold("SCREEN → GIF", nav) {
        SettingsCard {
            SettingsRow("Capture area", value = areas[area], chevron = true, divider = false, onClick = { area = (area + 1) % areas.size })
            SettingsRow("Include audio", trailing = { AppToggle(checked = audio) { audio = it } })
            SettingsRow("Max duration", value = "${durations[durIdx]}s", chevron = true, onClick = { durIdx = (durIdx + 1) % durations.size })
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                if (recording) "● REC 0:%02d — TAP TO STOP".format(elapsed)
                else "TAP TO START — AUTO-CONVERTS TO GIF",
                fontFamily = Mono,
                fontSize = 12.sp,
                color = if (recording) RecRed else InkMuted,
                modifier = if (recording) Modifier.alpha(pulseAlpha) else Modifier
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 34.dp)
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(RecRed)
                    .clickableNoRipple {
                        if (!recording) recording = true
                        else {
                            recording = false
                            addCreation(context, "screen_rec_${stamp()}.gif", "Screen Rec")
                        }
                    }
            ) {
                Box(
                    Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CardWhite)
                )
            }
        }
    }
}

/* ================= GIF EDITOR ================= */

@Composable
private fun EditorTool(nav: NavController) {
    var tool by rememberSaveable { mutableStateOf(-1) }
    var trimS by rememberSaveable { mutableStateOf(0.1f) }
    var trimE by rememberSaveable { mutableStateOf(0.7f) }
    var crop by rememberSaveable { mutableStateOf(0.6f) }
    var speed by rememberSaveable { mutableStateOf(1) }
    var reverse by rememberSaveable { mutableStateOf(false) }
    var caption by rememberSaveable { mutableStateOf("") }
    var sticker by rememberSaveable { mutableStateOf(0) }
    var filter by rememberSaveable { mutableStateOf(0) }
    var watermark by rememberSaveable { mutableStateOf("@you") }
    var wmOn by rememberSaveable { mutableStateOf(true) }

    val speeds = listOf("0.5×", "1×", "2×", "4×")
    val stickers = listOf("👍", "🔥", "😂", "💯")
    val filters = listOf("None", "Noir", "VHS", "Warm")

    ToolScaffold("GIF EDITOR", nav) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            GradientBox(
                0,
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .fillMaxWidth()
            ) {
                Content.editorTools.subList(0, 4).forEachIndexed { i, name ->
                    EditorCell(i, name, tool == i, Modifier.weight(1f)) { tool = if (tool == i) -1 else i }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Content.editorTools.subList(4, 8).forEachIndexed { i, name ->
                    EditorCell(i + 4, name, tool == i + 4, Modifier.weight(1f)) { tool = if (tool == i + 4) -1 else i + 4 }
                }
            }

            if (tool >= 0) {
                SettingsCard(modifier = Modifier.padding(top = 18.dp)) {
                    when (Content.editorTools[tool]) {
                        "TRIM" -> {
                            Column(Modifier.padding(16.dp)) {
                                SliderRow("Trim window", "%d%% – %d%%".format((trimS * 100).toInt(), (trimE * 100).toInt()), topPad = 0.dp)
                                TrimSlider(trimS, trimE, { s, e -> trimS = s; trimE = e })
                            }
                        }
                        "CROP" -> {
                            Column(Modifier.padding(16.dp)) {
                                SliderRow("Crop size", "${(crop * 100).toInt()}%", topPad = 0.dp)
                                GifSlider(crop, { crop = it })
                            }
                        }
                        "SPEED" -> {
                            Column(Modifier.padding(16.dp)) {
                                SliderRow("Playback speed", speeds[speed], topPad = 0.dp)
                                ChipRowInline(speeds, speed, { speed = it })
                            }
                        }
                        "REVERSE" -> SettingsRow("Play backwards", sub = "Reverse the frame order", divider = false, trailing = { AppToggle(reverse) { reverse = it } })
                        "CAPTION" -> SearchBar(hint = "Caption text...", value = caption, onValueChange = { caption = it }, topPad = 16.dp, hPad = 16.dp)
                        "STICKER" -> {
                            Column(Modifier.padding(16.dp)) {
                                SliderRow("Pick a sticker", stickers[sticker], topPad = 0.dp)
                                ChipRowInline(stickers, sticker, { sticker = it })
                            }
                        }
                        "FILTER" -> {
                            Column(Modifier.padding(16.dp)) {
                                SliderRow("Filter", filters[filter], topPad = 0.dp)
                                ChipRowInline(filters, filter, { filter = it })
                            }
                        }
                        "WATERMARK" -> {
                            SearchBar(hint = "@yourname", value = watermark, onValueChange = { watermark = it }, topPad = 16.dp, hPad = 16.dp)
                            SettingsRow("Show watermark", divider = false, trailing = { AppToggle(wmOn) { wmOn = it } })
                        }
                    }
                }
            }
        }
        YellowButton("CONTINUE TO EXPORT →") { nav.popBackStack() }
    }
}

@Composable
private fun EditorCell(index: Int, name: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickableNoRipple { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardWhite)
                .hardShadow(2.dp)
                .border(2.dp, if (selected) BgYellow else InkBlack, RoundedCornerShape(12.dp))
        ) {
            Text(Content.editorToolEmoji[index], fontSize = 17.sp)
        }
        Text(
            name,
            fontFamily = InterTight,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
            color = if (selected) InkBlack else InkMuted,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun ChipRowInline(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier, topPad: Dp = 8.dp) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.padding(top = topPad)) {
        options.forEachIndexed { i, opt ->
            val on = i == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (on) InkBlack else CardWhite)
                    .border(2.dp, InkBlack, RoundedCornerShape(16.dp))
                    .clickableNoRipple { onSelect(i) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(opt, fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = if (on) CardWhite else InkBlack)
            }
        }
    }
}

/* ================= MEME MAKER ================= */

@Composable
private fun MemeTool(nav: NavController, context: Context) {
    var top by rememberSaveable { mutableStateOf("TOP TEXT") }
    var bottom by rememberSaveable { mutableStateOf("BOTTOM TEXT") }
    var fontIdx by rememberSaveable { mutableStateOf(0) }

    ToolScaffold("MEME MAKER", nav) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Content.grads[0].first, Content.grads[0].second),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1000f)
                        )
                    )
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MemeText(top, fontIdx)
                    Spacer(Modifier.weight(1f))
                    MemeText(bottom, fontIdx)
                }
            }
            Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SearchBar(hint = "Top text...", value = top, onValueChange = { top = it }, topPad = 0.dp)
                SearchBar(hint = "Bottom text...", value = bottom, onValueChange = { bottom = it }, topPad = 0.dp)
            }
            ChipRow(Content.memeFonts, fontIdx, { fontIdx = it }, topPad = 0.dp)
        }
        ExportAction("EXPORT MEME →", { addCreation(context, "meme_${stamp()}.gif", "Meme Maker") })
    }
}

private fun memeFont(idx: Int): FontFamily = when (Content.memeFonts[idx]) {
    "Impact" -> FontFamily.SansSerif
    "Lilita" -> Lilita
    "Mono" -> Mono
    else -> FontFamily.Cursive
}

@Composable
private fun MemeText(text: String, fontIdx: Int) {
    val fam = memeFont(fontIdx)
    Box(Modifier.fillMaxWidth()) {
        listOf(Offset(0f, 2f), Offset(2f, 0f), Offset(-2f, 0f), Offset(0f, -2f)).forEach { off ->
            Text(
                text.uppercase(),
                fontFamily = fam,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .offset(x = off.x.dp, y = off.y.dp)
            )
        }
        Text(
            text.uppercase(),
            fontFamily = fam,
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
    }
}

/* ================= STICKER MAKER ================= */

@Composable
private fun StickerTool(nav: NavController, context: Context) {
    var autoBg by rememberSaveable { mutableStateOf(true) }
    var outline by rememberSaveable { mutableStateOf(false) }

    ToolScaffold("STICKER MAKER", nav) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val cell = 16.dp.toPx()
                    var r = 0
                    while (r * cell < size.height) {
                        var c = 0
                        while (c * cell < size.width) {
                            drawRect(
                                color = if ((r + c) % 2 == 0) Color(0xFFE8E8E8) else Color(0xFFCCCCCC),
                                topLeft = Offset(c * cell, r * cell),
                                size = Size(cell, cell)
                            )
                            c++
                        }
                        r++
                    }
                }
                if (!autoBg) {
                    // background removal off: show the original rectangle photo layer
                    GradientBox(
                        0,
                        Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    // cutout mask on transparency checkerboard
                    GradientBox(
                        0,
                        Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .then(if (outline) Modifier.border(4.dp, CardWhite, CircleShape) else Modifier)
                    )
                    Canvas(Modifier.size(158.dp)) {
                        drawCircle(
                            color = InkBlack,
                            radius = (74.dp.toPx()) + 2.dp.toPx(),
                            style = Stroke(
                                width = 3.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                            )
                        )
                    }
                }
            }
            SettingsCard(modifier = Modifier.padding(top = 16.dp)) {
                SettingsRow("Auto-remove background", divider = false, trailing = { AppToggle(autoBg) { autoBg = it } })
                SettingsRow("White border outline", trailing = { AppToggle(outline) { outline = it } })
            }
        }
        ExportAction("SAVE STICKER →", { addCreation(context, "sticker_${stamp()}.gif", "Sticker Maker") })
    }
}

/* ================= MERGE / COMBINE ================= */

@Composable
private fun MergeTool(nav: NavController, context: Context) {
    val clips = remember { mutableStateListOf(0, 1, 2) }
    var selClip by rememberSaveable { mutableStateOf(-1) }
    var layout by rememberSaveable { mutableStateOf(0) }

    ToolScaffold("MERGE CLIPS", nav) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            ) {
                when (layout) {
                    0 -> Row(Modifier.fillMaxSize()) {
                        GradientBox(clips.getOrElse(0) { 0 }, Modifier.weight(1f).fillMaxHeight())
                        GradientBox(clips.getOrElse(1) { 1 }, Modifier.weight(1f).fillMaxHeight())
                    }
                    1 -> Column(Modifier.fillMaxSize()) {
                        GradientBox(clips.getOrElse(0) { 0 }, Modifier.weight(1f).fillMaxWidth())
                        GradientBox(clips.getOrElse(1) { 1 }, Modifier.weight(1f).fillMaxWidth())
                    }
                    else -> Column(Modifier.fillMaxSize()) {
                        Row(Modifier.weight(1f)) {
                            GradientBox(clips.getOrElse(0) { 0 }, Modifier.weight(1f).fillMaxHeight())
                            GradientBox(clips.getOrElse(1) { 1 }, Modifier.weight(1f).fillMaxHeight())
                        }
                        Row(Modifier.weight(1f)) {
                            GradientBox(clips.getOrElse(2) { 2 }, Modifier.weight(1f).fillMaxHeight())
                            GradientBox(clips.getOrElse(0) { 0 }, Modifier.weight(1f).fillMaxHeight())
                        }
                    }
                }
            }
            MonoLabel("Timeline", topPad = 18.dp, bottomPad = 8.dp)
            Box(Modifier.fillMaxWidth()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(clips.size) { i ->
                    GradientBox(
                        clips[i],
                        Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, if (selClip == i) BgYellow else InkBlack, RoundedCornerShape(10.dp))
                            .clickableNoRipple { selClip = if (selClip == i) -1 else i }
                    )
                }
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardWhite)
                            .clickableNoRipple { clips.add(clips.size % Content.grads.size) }
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            drawRoundRect(
                                color = InkBlack,
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                                )
                            )
                        }
                        Text("+", fontSize = 22.sp, color = InkBlack)
                    }
                }
            }
                EdgeFade()
            }
            ChipRow(Content.mergeLayouts, layout, { layout = it }, topPad = 18.dp)
        }
        ExportAction("MERGE & EXPORT →", { addCreation(context, "merged_${stamp()}.gif", "Merge") })
    }
}
