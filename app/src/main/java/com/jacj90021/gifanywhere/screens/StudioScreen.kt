package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.BoxCard
import com.jacj90021.gifanywhere.components.FormatChips
import com.jacj90021.gifanywhere.components.PlatformChips
import com.jacj90021.gifanywhere.components.SingleToggle
import com.jacj90021.gifanywhere.components.SliderRow
import com.jacj90021.gifanywhere.components.SourceRow
import com.jacj90021.gifanywhere.components.StatusBar
import com.jacj90021.gifanywhere.components.ToolsGrid
import com.jacj90021.gifanywhere.components.TopBar
import com.jacj90021.gifanywhere.components.ToolItem
import com.jacj90021.gifanywhere.components.ActionSheet.buildSheetIcon
import com.jacj90021.gifanywhere.components.SourceItem

@Composable
fun StudioScreen(
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedFormatIndex by remember { mutableIntStateOf(0) }
    var selectedPlatformIndices by remember { mutableStateOf(setOf(0)) }
    var batchEnabled by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        StatusBar()
        Spacer(modifier = Modifier.height(4.dp))
        TopBar(title = "STUDIO")
        Spacer(modifier = Modifier.height(4.dp))

        // Source row
        LazyRow(
            modifier = Modifier.padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 0.dp, vertical = 4.dp),
        ) {
            itemsIndexed(listOf(
                SourceItem("Gallery", "M3 3 l18 0 l0 18 l-18 0 z M8.5 8.5 m-1.5 0 a1.5 1.5 0 1 1 3 0 a1.5 1.5 0 1 1 -3 0 M21 15 l-5 -5 l-5 5"),
                SourceItem("Camera", "M23 19 a2 2 0 0 1 -2 2 H3 a2 2 0 0 1 -2 -2 V8 a2 2 0 0 1 2 -2 h4 l2 -3 h6 l2 3 h4 a2 2 0 0 1 2 2 z"),
                SourceItem("Video", "M23 7 l-7 5 l7 5 l0 -12 z M1 5 l15 0 l0 14 l-15 0 z"),
                SourceItem("URL", "M10 13 a5 5 0 0 0 7.07 0 l2.83 -2.83 a5 5 0 0 0 -7.07 -7.07 l-1.72 1.71 M14 11 a5 5 0 0 0 -7.07 0 l-2.83 2.83 a5 5 0 0 0 7.07 7.07 l1.71 -1.71"),
            )) { _, item ->
                SourceRowRow(
                    item = item,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Tools grid
        ToolsGrid(
            items = listOf(
                ToolItem("Video → GIF", "Trim any clip into a loop", "M8 5 l6 0 l0 14 l-6 0 z"),
                ToolItem("Boomerang", "", "M17 2 l4 4 l-4 4 M3 11 V9 a4 4 0 0 1 4 -4 h14 M7 22 l-4 -4 l4 -4 M21 13 v2 a4 4 0 0 1 -4 4 h-14"),
                ToolItem("Screen Rec", "Record your screen", "M2 3 l20 0 l0 14 l-20 0 z M8 21 l8 0 l0 4 M12 17 v4"),
                ToolItem("GIF Editor", "", "M12 20 l9 0 M16.5 3.5 a2.1 2.1 0 0 1 3 3 l-9 16 l-4 1 l1 -4"),
                ToolItem("Meme Maker", "Add text overlays", "M4 7 V4 h16 v3 M9 20 h6 M12 4 v16"),
            ),
            modifier = Modifier.padding(vertical = 0.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))

        // BoxCard: Export Format + Platform Preset + Slider + Batch
        BoxCard() {
            Column {
                Text(
                    text = "Export Format",
                    style = androidx.compose.material3.Typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color(0xFF555555),
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                FormatChips(
                    options = listOf("GIF", "MP4", "WebP", "WebM"),
                    selectedIndex = selectedFormatIndex,
                    onSelected = { selectedFormatIndex = it },
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Platform Preset",
                    style = androidx.compose.material3.Typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color(0xFF555555),
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                )
                PlatformChips(
                    options = listOf("Discord", "Instagram", "WhatsApp"),
                    selectedIndices = selectedPlatformIndices,
                    onSelected = { idx ->
                        val next = if (idx in selectedPlatformIndices) selectedPlatformIndices - idx else selectedPlatformIndices + idx
                        selectedPlatformIndices = next
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                SliderRow(
                    label = "FPS Rate",
                    value = "24",
                    fillPercent = 0.6f,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(0.dp))
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                        .border(1.5.dp, androidx.compose.ui.graphics.Color(0xFF000000).copy(alpha = 0.12f), RoundedCornerShape(0.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Batch Export",
                        style = androidx.compose.material3.Typography.labelMedium,
                        color = androidx.compose.ui.graphics.Color.Black,
                    )
                    SingleToggle(
                        enabled = batchEnabled,
                        onToggle = { batchEnabled = !batchEnabled },
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Primary export button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            color = androidx.compose.ui.graphics.Color.Black,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "EXPORT GIF →",
                style = androidx.compose.material3.Typography.labelLarge,
                color = androidx.compose.ui.graphics.Color(0xFFFFD600),
                modifier = Modifier.padding(vertical = 14.dp),
                textAlign = androidx.compose.ui.text.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SourceRowRow(item: SourceItem, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(12.dp))
                .border(2.dp, androidx.compose.ui.graphics.Color.Black, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = buildSheetIcon(item.iconPath),
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.Black,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = item.label,
            style = androidx.compose.material3.Typography.bodySmall,
            color = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

