package com.jacj90021.gifanywhere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.data.Creation
import com.jacj90021.gifanywhere.data.RecentItem
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudioScreen(nav: NavController) {
    val context = LocalContext.current
    var showUrlDialog by remember { mutableStateOf(false) }
    var urlValue by rememberSaveable { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgYellow)
            .verticalScroll(rememberScrollState())
    ) {
        H1("STUDIO")

        // ---- Source picker row ----
        Box(Modifier.fillMaxWidth().padding(top = 14.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(Content.sources.size) { i ->
                    val name = Content.sources[i]
                    SourceButton(sourceIcon(name), name) {
                        when (name) {
                            "URL" -> showUrlDialog = true
                            else -> android.widget.Toast.makeText(context, "Opening $name…", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            EdgeFade()
        }

        // ---- 5-tile tools grid (exactly the mockup) ----
        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // big card spans both columns
            ToolCardBig(
                title = "Video → GIF",
                sub = "Trim any clip into a loop"
            ) { nav.navigate("tool/video") { launchSingleTop = true } }
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ToolCard("boomerang", "Boomerang", AppIcons.Repeat, Modifier.weight(1f)) { nav.navigate("tool/boomerang") { launchSingleTop = true } }
                ToolCard("screenrec", "Screen Rec", AppIcons.Monitor, Modifier.weight(1f)) { nav.navigate("tool/screenrec") { launchSingleTop = true } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ToolCard("editor", "GIF Editor", AppIcons.Studio, Modifier.weight(1f)) { nav.navigate("tool/editor") { launchSingleTop = true } }
                ToolCard("meme", "Meme Maker", AppIcons.Meme, Modifier.weight(1f)) { nav.navigate("tool/meme") { launchSingleTop = true } }
            }
        }

        ExportPanel()

        ExportAction(
            label = "EXPORT ${Store.exportFormat} →",
            toast = { msg -> android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show() },
            onExport = {
                val stamp = SimpleDateFormat("HHmmss", Locale.US).format(Date())
                val name = "export_$stamp.${Store.exportFormat.lowercase()}"
                Store.creations.add(0, Creation(name, "Studio"))
                Store.recent.add(0, RecentItem(name, (Store.creations.size) % Content.grads.size))
            }
        )
    }

    if (showUrlDialog) {
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            containerColor = CardWhite,
            titleContentColor = InkBlack,
            textContentColor = InkMuted,
            title = { Text("Add GIF URL", fontFamily = Lilita) },
            text = {
                androidx.compose.foundation.text.BasicTextField(
                    value = urlValue,
                    onValueChange = { urlValue = it },
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = InkBlack
                    ),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(InkBlack),
                    decorationBox = { inner ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardWhite)
                                .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            if (urlValue.isEmpty()) Text("https://…gif", color = InkMuted, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = InterTight)
                            inner()
                        }
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showUrlDialog = false
                    if (urlValue.isNotBlank()) {
                        android.widget.Toast.makeText(context, "URL source ready", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }) { Text("ADD", color = InkBlack, fontFamily = Mono, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("CANCEL", color = InkMuted, fontFamily = Mono, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/* ---------- Source picker ---------- */

private fun sourceIcon(name: String): ImageVector = when (name) {
    "Gallery" -> AppIcons.Image
    "Camera" -> AppIcons.Camera
    "Video" -> AppIcons.Video
    else -> AppIcons.Link
}

@Composable
private fun SourceButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(74.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardWhite)
                .hardShadow(3.dp)
                .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                .clickableNoRipple { onClick() }
        ) {
            Icon(icon, contentDescription = label, tint = InkBlack, modifier = Modifier.size(20.dp))
        }
        Text(
            label,
            fontFamily = InterTight,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = InkBlack,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/* ---------- Tool tiles ---------- */

@Composable
private fun ToolCardBig(title: String, sub: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .pressable { onClick() }
            .padding(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BgYellow)
                .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
        ) {
            Icon(AppIcons.Play, contentDescription = title, tint = InkBlack, modifier = Modifier.size(18.dp))
        }
        Column(Modifier.padding(start = 10.dp)) {
            Text(title, fontFamily = InterTight, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = InkBlack)
            Text(sub, fontFamily = InterTight, fontWeight = FontWeight.Medium, fontSize = 10.sp, color = InkMuted)
        }
    }
}

@Composable
private fun ToolCard(id: String, title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .pressable { onClick() }
            .padding(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BgYellow)
                .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
        ) {
            Icon(icon, contentDescription = title, tint = InkBlack, modifier = Modifier.size(18.dp))
        }
        Text(
            title,
            fontFamily = InterTight,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = InkBlack,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

/* ---------- Export panel ---------- */

@Composable
fun ExportPanel() {
    Column(
        Modifier
            .padding(horizontal = 18.dp, vertical = 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(
            "EXPORT FORMAT",
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 9.5.sp,
            color = InkMuted,
            letterSpacing = 0.5.sp
        )
        // format chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        ) {
            Content.formats.forEach { f ->
                val on = Store.exportFormat == f
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (on) InkBlack else CardWhite)
                        .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
                        .clickableNoRipple { Store.exportFormat = f }
                        .padding(vertical = 7.dp)
                ) {
                    Text(
                        f,
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        color = if (on) CardWhite else InkBlack
                    )
                }
            }
        }

        Text(
            "PLATFORM PRESET",
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 9.5.sp,
            color = InkMuted,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(top = 14.dp)
        )
        Box(Modifier.fillMaxWidth().padding(top = 10.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Content.platforms.size) { i ->
                    val name = Content.platforms[i]
                    val on = Store.platform == name
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (on) BgYellow else CardWhite)
                            .border(2.dp, InkBlack, RoundedCornerShape(16.dp))
                            .clickableNoRipple { Store.platform = if (on) null else name }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            name,
                            fontFamily = InterTight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = InkBlack
                        )
                    }
                }
            }
            // trailing fade signals more presets off-screen
            EdgeFade(color = CardWhite)
        }

        // FPS slider: 5..60 (mockup: single FPS Rate slider)
        SliderRow("FPS Rate", "${Store.fps.toInt()}")
        GifSlider(
            progress = (Store.fps - 5f) / 55f,
            onProgress = { Store.fps = 5f + it * 55f }
        )

        // batch row
        Spacer(Modifier.height(14.dp))
        DashedDivider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text("Batch Export", fontFamily = InterTight, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = InkBlack)
            AppToggle(checked = Store.batch) { Store.batch = it }
        }
    }
}
