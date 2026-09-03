package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.jacj90021.gifanywhere.components.sheetActions
import com.jacj90021.gifanywhere.components.ChipRow
import com.jacj90021.gifanywhere.components.GifCard
import com.jacj90021.gifanywhere.components.SearchBar
import com.jacj90021.gifanywhere.components.SegmentedGroup
import com.jacj90021.gifanywhere.components.StatusBar
import com.jacj90021.gifanywhere.components.TopBar
import com.jacj90021.gifanywhere.theme.Typography
import kotlin.math.roundToInt

@Composable
fun DiscoverScreen(
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeIndex by remember { mutableIntStateOf(0) }
    var selectedSortIndex by remember { mutableIntStateOf(0) }
    var selectedChipIndices by remember { mutableStateOf(setOf(0)) }
    var sheetVisible by remember { mutableStateOf(false) }

    val masonryHeights = listOf(140, 100, 110, 150, 120, 90)

    Column(modifier = modifier.fillMaxSize()) {
        StatusBar()
        Spacer(modifier = Modifier.height(4.dp))
        TopBar(title = "DISCOVER", tag = "BETA")
        Spacer(modifier = Modifier.height(4.dp))
        SearchBar(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Search GIFs, stickers, memes...",
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 0.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        SegmentedGroup(
            options = listOf("GIFs", "Stickers", "Memes"),
            selectedIndex = selectedTypeIndex,
            onSelected = { selectedTypeIndex = it },
            modifier = Modifier.padding(horizontal = 18.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        ChipRow(
            options = listOf("Trending", "Reactions", "Memes", "Anime", "Love", "Sports"),
            selectedIndices = selectedChipIndices,
            onSelected = { idx ->
                val next = if (idx in selectedChipIndices) selectedChipIndices - idx else selectedChipIndices + idx
                selectedChipIndices = next
            },
            modifier = Modifier.padding(vertical = 2.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        SegmentedGroup(
            options = listOf("Trending", "Favorites", "Recent"),
            selectedIndex = selectedSortIndex,
            onSelected = { selectedSortIndex = it },
            modifier = Modifier.padding(horizontal = 18.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Masonry grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 4.dp),
        ) {
            items(masonryHeights.size) { idx ->
                GifCard(
                    animationHeightDp = masonryHeights[idx].dp,
                    onClick = { sheetVisible = true },
                )
            }
        }

        // Full action sheet (backdrop + bottom sheet)
        if (sheetVisible) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .padding(top = 12.dp, bottom = 20.dp, start = 18.dp, end = 18.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(4.dp)
                            .background(Color.Black, RoundedCornerShape(2.dp))
                            .padding(top = 0.dp, bottom = 10.dp)
                            .graphicsLayer(
                                alpha = 0.3f
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFFFFD600))
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp)),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        repeat(2) { colIndex ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f),
                            ) {
                                sheetActions.chunked(3).getOrElse(colIndex) { emptyList() }.forEach { action ->
                                    Box(
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .size(44.dp)
                                            .background(
                                                if (action.isPrimary) Color(0xFFFFD600) else Color.White,
                                                RoundedCornerShape(12.dp),
                                            )
                                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                                            .clickable { sheetVisible = false },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxSize(),
                                        ) {                                                Icon(
                                                    painter = painterResource(action.iconRes),
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(18.dp),
                                                )
                                            Text(
                                                text = action.label,
                                                style = Typography.bodySmall,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
