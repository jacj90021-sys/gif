package com.jacj90021.gifanywhere.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
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

    Column(Modifier.fillMaxSize().background(BgYellow)) {
        H1("LIBRARY")
        Segment(listOf("Favorites", "Recent", "Creations"), seg, { seg = it }, topPad = 12.dp)

        AnimatedContent(
            targetState = seg,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                (fadeIn(tween(200)) + slideInVertically(tween(240)) { it / 22 })
                    .togetherWith(fadeOut(tween(120)))
            },
            label = "librarySegment"
        ) { currentSeg ->
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                when (currentSeg) {
                0 -> {
                    if (Store.folders.isEmpty()) {
                        EmptyState(
                            icon = AppIcons.Heart,
                            title = "NO FAVORITES YET.",
                            sub = "Tap the heart on any GIF in Discover and your keepers will collect here."
                        )
                    }
                    FolderGrid()
                }
                1 -> {
                    if (Store.recent.isEmpty()) {
                        EmptyState(
                            icon = AppIcons.Repeat,
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
                            icon = AppIcons.Studio,
                            title = "YOUR STUDIO IS EMPTY.",
                            sub = "Trim a video, caption a meme or record your screen — everything you export lands right here."
                        )
                    } else {
                        CreationsGrid()
                    }
                }
            }

            // ---- Wallpapers ----
            H1("WALLPAPERS", size = 18.sp, topPad = 10.dp, bottomPad = 2.dp)
            WallpaperSection(context)
            // ---- Storage ----
            StorageCard(context)
            Spacer(Modifier.height(32.dp))  // clears the bottom nav + system gesture bar
            }
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
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardWhite)
                    .hardShadow(3.dp)
                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                    .clickableNoRipple {
                        Store.folders.add(Folder("New Folder ${Store.folders.size + 1}", 0, 4))
                    }
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        "+",
                        fontFamily = Mono,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        color = InkBlack
                    )
                    Text(
                        "New Folder",
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = InkBlack
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
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        GradientBox(f.gradIdx, Modifier.fillMaxSize())
        Text(
            f.count.toString(),
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 9.sp,
            color = InkBlack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(6.dp))
                .background(BgYellow)
                .border(1.5.dp, InkBlack, RoundedCornerShape(6.dp))
                .padding(horizontal = 5.dp, vertical = 1.dp)
        )
        Text(
            f.name,
            fontFamily = InterTight,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = OffWhite,
            modifier = Modifier.padding(top = 40.dp)
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardWhite)
                            .hardShadow(3.dp)
                            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            c.name,
                            fontFamily = Mono,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            color = InkBlack
                        )
                        Text(
                            c.tool.uppercase(),
                            fontFamily = Mono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                            color = InkMuted,
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
private fun EmptyState(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, sub: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 40.dp)
    ) {
        androidx.compose.material3.Icon(
            icon,
            contentDescription = null,
            tint = InkBlack.copy(alpha = 0.15f),
            modifier = Modifier.size(64.dp)
        )
        Text(
            title,
            fontFamily = Lilita,
            fontSize = 18.sp,
            color = InkBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 18.dp)
        )
        Text(
            sub,
            fontFamily = InterTight,
            fontWeight = FontWeight.Medium,
            fontSize = 12.5.sp,
            color = InkMuted,
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (on) BgYellow else CardWhite)
                    .hardShadow(2.dp)
                    .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
                    .clickableNoRipple { Store.wallTarget = t }
                    .padding(vertical = 7.dp)
            ) {
                Text(
                    t,
                    fontFamily = InterTight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    color = InkBlack
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
                GradientBox(gi, Modifier.fillMaxSize().border(2.dp, InkBlack, RoundedCornerShape(12.dp)))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(BgYellow)
                        .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp))
                        .clickableNoRipple {
                            setLabel = "✓ SET"
                            Toast.makeText(context, "${Store.wallTarget} wallpaper set", Toast.LENGTH_SHORT).show()
                        }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        setLabel,
                        fontFamily = InterTight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 8.sp,
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
            .padding(horizontal = 18.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Cache Storage",
                fontFamily = InterTight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = InkBlack,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${Store.cacheMB.toInt()} MB",
                fontFamily = Mono,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = InkBlack
            )
        }
        Box(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(InkBlack.copy(alpha = 0.08f))
                .border(2.dp, InkBlack, RoundedCornerShape(3.dp))
        ) {
            Box(
                Modifier
                    .fillMaxWidth((Store.cacheMB / 1000f).coerceIn(0.02f, 1f))
                    .height(6.dp)
                    .background(BgYellow)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CardWhite)
                .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
                .clickableNoRipple {
                    Store.cacheMB = 12f
                    Toast.makeText(context, "Cache cleared — 368 MB freed", Toast.LENGTH_SHORT).show()
                }
                .padding(vertical = 8.dp)
        ) {
            Text(
                "CLEAR CACHE",
                fontFamily = InterTight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = InkBlack
            )
        }
    }
}
