package com.jacj90021.gifanywhere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
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
            .background(InkBlack)
            .verticalScroll(rememberScrollState())
    ) {
        H1("STUDIO")

        // ---- Source picker row ----
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.padding(top = 14.dp)
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

        // ---- 7-tile tools grid ----
        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // big card spans both columns
            ToolCardBig(
                title = "Video → GIF",
                sub = "Trim any clip into a loop"
            ) { nav.navigate("tool/video") }
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ToolCard("boomerang", "Boomerang", Icons.Filled.PhotoCamera, Modifier.weight(1f)) { nav.navigate("tool/boomerang") }
                ToolCard("screenrec", "Screen Rec", Icons.Filled.DesktopWindows, Modifier.weight(1f)) { nav.navigate("tool/screenrec") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ToolCard("editor", "GIF Editor", Icons.Filled.Edit, Modifier.weight(1f)) { nav.navigate("tool/editor") }
                ToolCard("meme", "Meme Maker", Icons.Filled.Create, Modifier.weight(1f)) { nav.navigate("tool/meme") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ToolCard("sticker", "Sticker Maker", Icons.Filled.Favorite, Modifier.weight(1f)) { nav.navigate("tool/sticker") }
                ToolCard("merge", "Merge / Combine", Icons.Filled.AccountTree, Modifier.weight(1f)) { nav.navigate("tool/merge") }
            }
        }

        ExportPanel()

        ExportAction(
            label = "EXPORT ${Store.exportFormat} →",
            toast = { msg -> android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show() }
        ) {
            val stamp = SimpleDateFormat("HHmmss", Locale.US).format(Date())
            val name = "export_$stamp.${Store.exportFormat.lowercase()}"
            Store.creations.add(0, Creation(name, "Studio"))
            Store.recent.add(0, RecentItem(name, (Store.creations.size) % Content.grads.size))
        }
    }

    if (showUrlDialog) {
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            containerColor = Charcoal,
            titleContentColor = OffWhite,
            textContentColor = OffDim,
            title = { Text("Add GIF URL", fontFamily = Lilita) },
            text = {
                androidx.compose.foundation.text.BasicTextField(
                    value = urlValue,
                    onValueChange = { urlValue = it },
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = InterTight, fontSize = 13.sp, color = OffWhite
                    ),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Yellow),
                    decorationBox = { inner ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Charcoal2)
                                .border(2.dp, LineColor, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            if (urlValue.isEmpty()) Text("https://…gif", color = OffFaint, fontSize = 13.sp, fontFamily = InterTight)
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
                }) { Text("ADD", color = Yellow, fontFamily = Mono, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("CANCEL", color = OffFaint, fontFamily = Mono, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/* ---------- Source picker ---------- */

private fun sourceIcon(name: String): ImageVector = when (name) {
    "Gallery" -> Icons.Filled.Image
    "Camera" -> Icons.Filled.PhotoCamera
    "Video" -> Icons.Filled.Videocam
    "URL" -> Icons.Filled.Link
    "Library" -> Icons.Filled.PhotoLibrary
    else -> Icons.Filled.DesktopWindows
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
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Charcoal)
                .border(2.dp, LineColor, RoundedCornerShape(18.dp))
                .clickableNoRipple { onClick() }
        ) {
            Icon(icon, contentDescription = label, tint = Yellow, modifier = Modifier.size(24.dp))
        }
        Text(
            label,
            fontFamily = InterTight,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = OffDim,
            modifier = Modifier.padding(top = 7.dp)
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
            .clip(RoundedCornerShape(18.dp))
            .background(Yellow)
            .clickableNoRipple { onClick() }
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(InkBlack)
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = title, tint = Yellow, modifier = Modifier.size(18.dp))
        }
        Column(Modifier.padding(start = 14.dp)) {
            Text(title, fontFamily = InterTight, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = InkBlack)
            Text(sub, fontFamily = InterTight, fontSize = 10.5.sp, color = InkBlack.copy(alpha = 0.65f))
        }
    }
}

@Composable
private fun ToolCard(id: String, title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Charcoal)
            .border(2.dp, LineColor, RoundedCornerShape(18.dp))
            .clickableNoRipple { onClick() }
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Yellow)
        ) {
            Icon(icon, contentDescription = title, tint = InkBlack, modifier = Modifier.size(18.dp))
        }
        Text(
            title,
            fontFamily = InterTight,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.5.sp,
            color = OffWhite,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

/* ---------- Export panel ---------- */

@Composable
fun ExportPanel() {
    Column(
        Modifier
            .padding(horizontal = 20.dp, vertical = 0.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Charcoal)
            .border(2.dp, LineColor, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Text(
            "EXPORT",
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = OffFaint,
            letterSpacing = 1.sp
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (on) Yellow else Charcoal2)
                        .border(2.dp, if (on) Yellow else LineColor, RoundedCornerShape(12.dp))
                        .clickableNoRipple { Store.exportFormat = f }
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        f,
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.5.sp,
                        color = if (on) InkBlack else OffWhite
                    )
                }
            }
        }

        Text(
            "PLATFORM PRESET",
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = OffFaint,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            items(Content.platforms.size) { i ->
                val name = Content.platforms[i]
                val on = Store.platform == name
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (on) Yellow else Charcoal2)
                        .border(2.dp, if (on) Yellow else LineColor, RoundedCornerShape(20.dp))
                        .clickableNoRipple { Store.platform = if (on) null else name }
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                ) {
                    Text(
                        name,
                        fontFamily = InterTight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (on) InkBlack else OffDim
                    )
                }
            }
        }

        // Colors slider: 2..256
        SliderRow("Colors", "${Store.colors.toInt()}")
        GifSlider(
            progress = (Store.colors - 2f) / 254f,
            onProgress = { Store.colors = 2f + it * 254f }
        )
        // FPS slider: 5..60
        SliderRow("FPS", "${Store.fps.toInt()}", topPad = 16.dp)
        GifSlider(
            progress = (Store.fps - 5f) / 55f,
            onProgress = { Store.fps = 5f + it * 55f }
        )
        // Target size slider: 0.5..20 MB
        SliderRow("Target size", if (Store.targetMB < 1f) "${(Store.targetMB * 1000).toInt()} KB" else "${"%.1f".format(Store.targetMB)} MB", topPad = 16.dp)
        GifSlider(
            progress = (Store.targetMB - 0.5f) / 19.5f,
            onProgress = { Store.targetMB = 0.5f + it * 19.5f }
        )

        // batch row
        Spacer(Modifier.height(18.dp))
        Box(Modifier.fillMaxWidth().height(2.dp).background(LineColor))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Batch export", fontFamily = InterTight, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = OffWhite)
            AppToggle(checked = Store.batch) { Store.batch = it }
        }
    }
}
