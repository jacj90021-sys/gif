package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 8.dp),
        ) {
            TopBar(title = "STUDIO")
        }
        Spacer(modifier = Modifier.height(6.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
        ) {
            SourceRow(
                items = listOf(
                    SourceItem(AppIcons.Gallery, "Gallery"),
                    SourceItem(AppIcons.Camera, "Camera"),
                    SourceItem(AppIcons.Video, "Video"),
                    SourceItem(AppIcons.Url, "URL"),
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            ToolsGrid(
                items = listOf(
                    ToolItem(AppIcons.VideoToGif, "Video → GIF", "Trim any clip into a loop", isBig = true),
                    ToolItem(AppIcons.Boomerang, "Boomerang", "Swing forward & back"),
                    ToolItem(AppIcons.ScreenRec, "Screen Rec", "Record your screen"),
                    ToolItem(AppIcons.GifEditor, "GIF Editor", "Trim frames"),
                    ToolItem(AppIcons.MemeMaker, "Meme Maker", "Add text overlays"),
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            BoxCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "EXPORT FORMAT",
                        style = Typography.bodySmall,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    FormatChips(
                        options = listOf("GIF", "MP4", "WebP", "WebM"),
                        selectedIndex = selectedFormatIndex,
                        onSelected = { selectedFormatIndex = it },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "PLATFORM PRESET",
                        style = Typography.bodySmall,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                    PlatformChips(
                        options = listOf("Discord", "Instagram", "WhatsApp"),
                        selectedIndices = selectedPlatformIndices,
                        onSelected = { idx ->
                            val next = if (idx in selectedPlatformIndices) {
                                selectedPlatformIndices - idx
                            } else {
                                selectedPlatformIndices + idx
                            }
                            selectedPlatformIndices = next
                        },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SliderRow(
                        label = "FPS RATE",
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
                            text = "BATCH EXPORT",
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
                    .padding(horizontal = 18.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { },
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
}
