package com.jacj90021.gifanywhere.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.data.Creation
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.data.Folder
import com.jacj90021.gifanywhere.data.RecentItem
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.theme.*

@Composable
fun LibraryScreen(nav: NavController) {
    val context = LocalContext.current
    var seg by rememberSaveable { mutableStateOf(0) }

    Column(Modifier.fillMaxSize().background(InkBlack)) {
        H1("LIBRARY")
        Segment(listOf("Favorites", "Recent", "Creations"), seg, { seg = it }, topPad = 12.dp)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (seg) {
                0 -> {
                    if (Store.folders.isEmpty()) {
                        EmptyState(
                            mark = "♡",
                            title = "NO FAVORITES YET.",
                            sub = "Tap the heart on any GIF in Discover and your keepers will collect here."
                        )
                    }
                    FolderGrid()
                }
                1 -> {
                    if (Store.recent.isEmpty()) {
                        EmptyState(
                            mark = "↻",
                            title = "NOTHING RECENT.",
                            sub = "Everything you view or save in the last 30 days will show up in this feed."
                        )
                    } else {
                        RecentGrid()
                    }
                }
                else -> {
                    if (Store.creations.isEmpty()) {
                        EmptyState(
                            mark = "✎",
                            title = "YOUR STUDIO IS EMPTY.",
                            sub = "Trim a video, caption a meme or record your screen — everything you export lands right here."
                        )
                    } else {
                        CreationsGrid()
                    }
                }
            }

            // ---- Wallpapers ----
            H1("WALLPAPERS", size = 20.sp, topPad = 10.dp, bottomPad = 2.dp)
            WallpaperSection(context)
            // ---- Storage ----
            StorageCard(context)
            Spacer(Modifier.height(32.dp))  // clears the bottom nav + system gesture bar
        }
    }
}

/* ---------- Folder collections ---------- */

@Composable
private fun FolderGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        val rows = Store.folders.chunked(2)
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { f ->
                    FolderCard(f, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        // New folder tile
        Row {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(Content.grads[3].first, Content.grads[3].second),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                        )
                    )
                    .border(2.dp, LineColor, RoundedCornerShape(18.dp))
                    .clickableNoRipple {
                        Store.folders.add(Folder("New Folder ${Store.folders.size + 1}", 0, 4))
                    }
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        "+",
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        color = Yellow
                    )
                    Text(
                        "New Folder",
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.5.sp,
                        color = OffWhite
                    )
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun FolderCard(f: Folder, modifier: Modifier = Modifier) {
    val g = Content.grads[f.gradIdx % 5]
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(g.first, g.second),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                )
            )
            .border(2.dp, LineColor, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            f.count.toString(),
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
            color = Yellow,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Text(
            f.name,
            fontFamily = InterTight,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.5.sp,
            color = OffWhite
        )
    }
}

/* ---------- Recent grid ---------- */

@Composable
private fun RecentGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(11.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Store.recent.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                row.forEach { r ->
                    GradientBox(
                        r.gradIdx,
                        Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        LoopBadge(Modifier.align(Alignment.TopEnd).padding(8.dp))
                        Text(
                            r.title,
                            fontFamily = InterTight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = OffWhite,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

/* ---------- Creations grid ---------- */

@Composable
private fun CreationsGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(11.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Store.creations.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                row.forEach { c: Creation ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Charcoal)
                            .border(2.dp, LineColor, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            c.name,
                            fontFamily = Mono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Yellow
                        )
                        Text(
                            c.tool.uppercase(),
                            fontFamily = Mono,
                            fontSize = 8.5.sp,
                            color = OffFaint,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

/* ---------- Empty states (unique copy per section) ---------- */

@Composable
private fun EmptyState(mark: String, title: String, sub: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 40.dp)
    ) {
        Text(mark, fontFamily = Lilita, fontSize = 64.sp, color = Charcoal, textAlign = TextAlign.Center)
        Text(
            title,
            fontFamily = Lilita,
            fontSize = 18.sp,
            color = OffWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 18.dp)
        )
        Text(
            sub,
            fontFamily = InterTight,
            fontSize = 12.5.sp,
            color = OffFaint,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/* ---------- Wallpapers ---------- */

@Composable
private fun WallpaperSection(context: android.content.Context) {
    val targets = listOf("Home", "Lock", "Both")
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        targets.forEach { t ->
            val on = Store.wallTarget == t
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (on) Yellow else Charcoal)
                    .border(2.dp, if (on) Yellow else LineColor, RoundedCornerShape(12.dp))
                    .clickableNoRipple { Store.wallTarget = t }
                    .padding(vertical = 9.dp)
            ) {
                Text(
                    t,
                    fontFamily = InterTight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = if (on) InkBlack else OffDim
                )
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        com.jacj90021.gifanywhere.data.Content.wallpaperGrads.forEach { gi ->
            var setLabel by remember { mutableStateOf("SET") }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                GradientBox(gi, Modifier.fillMaxSize())
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Yellow)
                        .clickableNoRipple {
                            setLabel = "✓ SET"
                            Toast.makeText(context, "${Store.wallTarget} wallpaper set", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        setLabel,
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 8.5.sp,
                        color = InkBlack
                    )
                }
            }
        }
    }
}

/* ---------- Storage manager ---------- */

@Composable
private fun StorageCard(context: android.content.Context) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Charcoal)
            .border(2.dp, LineColor, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Cache storage",
                fontFamily = InterTight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.5.sp,
                color = OffWhite,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${Store.cacheMB.toInt()} MB",
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Yellow
            )
        }
        Box(
            Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Charcoal2)
        ) {
            Box(
                Modifier
                    .fillMaxWidth((Store.cacheMB / 1000f).coerceIn(0.02f, 1f))
                    .height(8.dp)
                    .background(Yellow)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, Yellow, RoundedCornerShape(12.dp))
                .clickableNoRipple {
                    Store.cacheMB = 12f
                    Toast.makeText(context, "Cache cleared — 368 MB freed", Toast.LENGTH_SHORT).show()
                }
                .padding(vertical = 11.dp)
        ) {
            Text(
                "CLEAR CACHE",
                fontFamily = InterTight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = Yellow
            )
        }
    }
}
