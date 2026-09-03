package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.AppIcons
import com.jacj90021.gifanywhere.components.BoxCard
import com.jacj90021.gifanywhere.components.FormatChips
import com.jacj90021.gifanywhere.components.PlatformChips
import com.jacj90021.gifanywhere.components.SingleToggle
import com.jacj90021.gifanywhere.components.SliderRow
import com.jacj90021.gifanywhere.components.SourceItem
import com.jacj90021.gifanywhere.components.SourceRow
import com.jacj90021.gifanywhere.components.StatusBar
import com.jacj90021.gifanywhere.components.ToolItem
import com.jacj90021.gifanywhere.components.ToolsGrid
import com.jacj90021.gifanywhere.components.TopBar
import com.jacj90021.gifanywhere.theme.Typography

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

        LazyRow(
            modifier = Modifier.padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
        ) {
            itemsIndexed(listOf(
                SourceItem(AppIcons.Gallery, "Gallery"),
                SourceItem(AppIcons.Camera, "Camera"),
                SourceItem(AppIcons.Video, "Video"),
                SourceItem(AppIcons.Url, "URL"),
            )) { _, item ->
                SourceRowRow(
                    item = item,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        ToolsGrid(
            items = listOf(
                ToolItem(AppIcons.VideoToGif, "Video → GIF", "Trim any clip into a loop"),
                ToolItem(AppIcons.Boomerang, "Boomerang", ""),
                ToolItem(AppIcons.ScreenRec, "Screen Rec", "Record your screen"),
                ToolItem(AppIcons.GifEditor, "GIF Editor", ""),
                ToolItem(AppIcons.MemeMaker, "Meme Maker", "Add text overlays"),
            ),
            modifier = Modifier.padding(vertical = 0.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))

        BoxCard() {
            Column {
                Text(
                    text = "Export Format",
                    style = Typography.bodySmall,
                    color = Color(0xFF555555),
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
                    style = Typography.bodySmall,
                    color = Color(0xFF555555),
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
                        .background(Color.Transparent)
                        .border(1.5.dp, Color.Black.copy(alpha = 0.12f), RoundedCornerShape(0.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Batch Export",
                        style = Typography.labelMedium,
                        color = Color.Black,
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

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            color = Color.Black,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "EXPORT GIF →",
                style = Typography.labelLarge,
                color = Color(0xFFFFD600),
                modifier = Modifier.padding(vertical = 14.dp),
                textAlign = TextAlign.Center,
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
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = item.label,
            style = Typography.bodySmall,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
