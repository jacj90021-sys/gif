package com.jacj90021.gifanywhere.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.data.GifItem
import com.jacj90021.gifanywhere.data.RecentItem
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.theme.*

/** Mockup DISCOVER screen: search, segments, chips, masonry + action sheet. */
@Composable
fun DiscoverScreen(nav: NavController) {
    val context = LocalContext.current
    var kind by rememberSaveable { mutableStateOf(0) }
    var category by rememberSaveable { mutableStateOf(0) }
    var listSeg by rememberSaveable { mutableStateOf(0) }
    var query by rememberSaveable { mutableStateOf("") }
    var sheetItemId by rememberSaveable { mutableStateOf<Int?>(null) }
    val sheetItem = sheetItemId?.let { id -> Content.gifs.firstOrNull { it.id == id } }

    Column(Modifier.fillMaxSize().background(BgYellow)) {
        StatusBarRow()
        H1("DISCOVER", eyebrow = "BETA")

        SearchBar(
            hint = "Search GIFs, stickers, memes...",
            value = query,
            onValueChange = { query = it }
        )

        Segment(Content.kinds, kind, { kind = it }, topPad = 10.dp)
        ChipRow(Content.categories, category, { category = it }, topPad = 10.dp)
        Segment(listOf("Trending", "Favorites", "Recent"), listSeg, { listSeg = it }, topPad = 10.dp)

        val base = Content.gifs.filter { g ->
            g.kind == Content.kinds[kind] &&
                (category == 0 || g.category == Content.categories[category]) &&
                (query.isBlank() || g.title.contains(query, ignoreCase = true))
        }
        val items: List<Any> = when (listSeg) {
            1 -> base.filter { Store.favIds.contains(it.id) }
            2 -> Store.recent.map { r -> RecentView(r) }
            else -> base
        }

        // mockup .masonry: 2-col, 10dp gap, 18dp side padding
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 10.dp,
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 28.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { entry ->
                Box(Modifier.animateItem()) {
                    when (entry) {
                        is GifItem -> GifCard(entry) { sheetItemId = entry.id }
                        is RecentView -> RecentCard(entry) {
                            sheetItemId = (Content.gifs.firstOrNull { it.title == entry.title } ?: Content.gifs[0]).id
                        }
                    }
                }
            }
            if (items.isEmpty()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    val msg = when (listSeg) {
                        1 -> "NO FAVORITES IN THIS FILTER.\nTap the heart on any GIF to keep it here."
                        2 -> "NOTHING RECENT.\nWhat you view or save shows up in this feed."
                        else -> "NO MATCHES.\nTry another category or search term."
                    }
                    Text(
                        msg,
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = InkMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp)
                    )
                }
            }
        }
    }

    sheetItem?.let { item ->
        ActionSheet(item = item, onDismiss = { sheetItemId = null }, nav = nav)
    }
}

/** mockup .statusbar — 9:41 + signal dots, JetBrains Mono. */
@Composable
fun StatusBarRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 2.dp)
    ) {
        Text("9:41", fontFamily = Mono, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = InkBlack)
        Text("●●●", fontFamily = Mono, fontWeight = FontWeight.ExtraBold, fontSize = 8.sp, color = InkBlack)
    }
}

/** mockup .gif-card — white card, 2px ink border, hard shadow, LOOP badge. */
@Composable
private fun GifCard(item: GifItem, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(item.heightDp.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .clickableNoRipple { onClick() }
    ) {
        GradientBox(item.gradIdx, Modifier.fillMaxSize())
        Text(
            item.title,
            fontFamily = InterTight,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = InkBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 8.dp, end = 44.dp)
        )
        LoopBadge(Modifier.align(Alignment.TopEnd).padding(6.dp))
    }
}

@Composable
private fun RecentCard(item: RecentView, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(item.heightDp.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .hardShadow(3.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
            .clickableNoRipple { onClick() }
    ) {
        GradientBox(item.gradIdx, Modifier.fillMaxSize())
        Text(
            item.title,
            fontFamily = InterTight,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = InkBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 8.dp, end = 44.dp)
        )
        LoopBadge(Modifier.align(Alignment.TopEnd).padding(6.dp))
    }
}

/** mockup .loop-badge — white, 2px ink border, mono. */
@Composable
fun LoopBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CardWhite)
            .border(2.dp, InkBlack, RoundedCornerShape(8.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            "↻ LOOP",
            fontFamily = Mono,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 8.sp,
            color = InkBlack
        )
    }
}

private data class RecentView(val title: String, val gradIdx: Int, val heightDp: Int)

private fun RecentView(r: RecentItem) = RecentView(r.title, r.gradIdx, 120 + (r.title.length * 6) % 90)

/* ---------- Action sheet (mockup .sheet) ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionSheet(item: GifItem, onDismiss: () -> Unit, nav: NavController) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 32.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(InkBlack.copy(alpha = 0.3f))
            )
        }
    ) {
        // mockup .sheet-preview — yellow, 2px ink border, 120dp
        GradientBox(
            item.gradIdx,
            Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(18.dp))
        val rows = listOf(
            listOf("Send", "Save", "Favorite"),
            listOf("Edit", "Convert", "Wallpaper")
        )
        Column(Modifier.padding(horizontal = 18.dp, vertical = 4.dp)) {
            rows.forEachIndexed { _, row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { label ->
                        val primary = label == "Send"
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (primary) BgYellow else CardWhite)
                                    .hardShadow(2.dp)
                                    .border(2.dp, InkBlack, RoundedCornerShape(12.dp))
                                    .clickableNoRipple {
                                        onDismiss()
                                        when (label) {
                                            "Send" -> {
                                                val send = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_TEXT, "Check out this GIF: ${item.title} 🎞")
                                                }
                                                context.startActivity(Intent.createChooser(send, "Send GIF"))
                                            }
                                            "Save" -> {
                                                Store.recent.add(0, RecentItem(item.title, item.gradIdx))
                                                Toast.makeText(context, "Saved to Library", Toast.LENGTH_SHORT).show()
                                            }
                                            "Favorite" -> {
                                                Store.toggleFavorite(item.id)
                                                Toast.makeText(
                                                    context,
                                                    if (Store.favIds.contains(item.id)) "Added to Favorites" else "Removed from Favorites",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            "Edit" -> nav.navigate("tool/editor")
                                            "Convert" -> nav.navigate("studio")
                                            "Wallpaper" -> nav.navigate("library")
                                        }
                                    }
                            ) {
                                Icon(
                                    sheetIcon(label),
                                    contentDescription = label,
                                    tint = InkBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                label,
                                fontFamily = InterTight,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.5.sp,
                                color = InkBlack,
                                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(26.dp))
    }
}

private fun sheetIcon(label: String): ImageVector = when (label) {
    "Send" -> AppIcons.Send
    "Save" -> AppIcons.Bookmark
    "Favorite" -> AppIcons.Heart
    "Edit" -> AppIcons.Studio
    "Convert" -> AppIcons.Repeat
    else -> AppIcons.Image
}
