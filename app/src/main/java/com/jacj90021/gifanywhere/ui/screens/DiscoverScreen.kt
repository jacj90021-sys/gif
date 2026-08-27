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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacj90021.gifanywhere.data.Content
import com.jacj90021.gifanywhere.data.GifItem
import com.jacj90021.gifanywhere.data.RecentItem
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.*
import com.jacj90021.gifanywhere.ui.theme.*
import com.jacj90021.gifanywhere.R

@Composable
fun DiscoverScreen(nav: NavController) {
    val context = LocalContext.current
    var kind by rememberSaveable { mutableStateOf(0) }
    var category by rememberSaveable { mutableStateOf(0) }
    var listSeg by rememberSaveable { mutableStateOf(0) }
    var query by rememberSaveable { mutableStateOf("") }
    var sheetItem by remember { mutableStateOf<GifItem?>(null) }

    Column(Modifier.fillMaxSize().background(InkBlack)) {
        H1("DISCOVER")
        SearchBar(
            hint = "Search GIFs, stickers, memes...",
            value = query,
            onValueChange = { query = it },
            topPad = 14.dp
        )
        Segment(Content.kinds, kind, { kind = it }, topPad = 12.dp)
        ChipRow(Content.categories, category, { category = it }, topPad = 14.dp)
        Segment(listOf("Trending", "Favorites", "Recent"), listSeg, { listSeg = it }, topPad = 12.dp)

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

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalItemSpacing = 11.dp,
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 28.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { entry ->
                when (entry) {
                    is GifItem -> GifCard(entry) { sheetItem = entry }
                    is RecentView -> RecentCard(entry) { sheetItem = Content.gifs.firstOrNull { it.title == entry.title } ?: Content.gifs[0] }
                }
            }
            if (items.isEmpty()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    val msg = when (listSeg) {
                        1 -> "NO FAVORITES IN THIS FILTER.\nDouble-tap a card's ❤ action and it lands here."
                        2 -> "NOTHING RECENT.\nWhat you view or save shows up in this feed."
                        else -> "NO MATCHES.\nTry another category or search term."
                    }
                    Text(
                        msg,
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = OffFaint,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp)
                    )
                }
            }
        }
    }

    sheetItem?.let { item ->
        ActionSheet(item = item, onDismiss = { sheetItem = null }, nav = nav)
    }
}

@Composable
private fun GifCard(item: GifItem, onClick: () -> Unit) {
    GradientBox(item.gradIdx, Modifier.fillMaxWidth().height(item.heightDp.dp).clip(RoundedCornerShape(16.dp)).clickableNoRipple { onClick() }) {
        LoopBadge(Modifier.align(Alignment.TopEnd).padding(8.dp))
    }
}

@Composable
private fun RecentCard(item: RecentView, onClick: () -> Unit) {
    GradientBox(item.gradIdx, Modifier.fillMaxWidth().height(item.heightDp.dp).clip(RoundedCornerShape(16.dp)).clickableNoRipple { onClick() }) {
        LoopBadge(Modifier.align(Alignment.TopEnd).padding(8.dp))
    }
}

@Composable
fun LoopBadge(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xB30A0A0A))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            "↻ LOOP",
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 8.5.sp,
            color = Yellow
        )
    }
}

private data class RecentView(val title: String, val gradIdx: Int, val heightDp: Int)

private fun RecentView(r: RecentItem) = RecentView(r.title, r.gradIdx, 120 + (r.title.length * 6) % 90)

/* ---------- Action sheet ---------- */

private data class SheetActionDef(val label: String, val emojiIcon: (() -> Unit)? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionSheet(item: GifItem, onDismiss: () -> Unit, nav: NavController) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Charcoal2,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(OffFaint)
            )
        }
    ) {
        GradientBox(item.gradIdx, Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)))
        Spacer(Modifier.height(18.dp))
        val rows = listOf(
            listOf("Send", "Save", "Favorite"),
            listOf("Edit", "Convert", "Wallpaper")
        )
        Column(Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
            rows.forEachIndexed { rowIdx, row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { label ->
                        val primary = label == "Send"
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (primary) Yellow else Charcoal)
                                    .border(2.dp, if (primary) Yellow else LineColor, RoundedCornerShape(16.dp))
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
                                Text(
                                    sheetEmoji(label),
                                    fontSize = 20.sp,
                                    color = if (primary) InkBlack else Yellow
                                )
                            }
                            Text(
                                label,
                                fontFamily = InterTight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = OffWhite,
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

private fun sheetEmoji(label: String): String = when (label) {
    "Send" -> "➤"
    "Save" -> "⚑"
    "Favorite" -> "♥"
    "Edit" -> "✎"
    "Convert" -> "⇄"
    else -> "🖼"
}
