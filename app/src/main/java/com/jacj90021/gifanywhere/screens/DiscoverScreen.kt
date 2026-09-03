package com.jacj90021.gifanywhere.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.ActionSheet
import com.jacj90021.gifanywhere.components.ChipRow
import com.jacj90021.gifanywhere.components.GifCard
import com.jacj90021.gifanywhere.components.SearchBar
import com.jacj90021.gifanywhere.components.SegmentedGroup
import com.jacj90021.gifanywhere.components.TopBar

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

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Real system status bar inset, then header
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 8.dp),
            ) {
                TopBar(title = "DISCOVER", tag = "BETA")
                Spacer(modifier = Modifier.height(6.dp))
                SearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search GIFs, stickers, memes...",
                    modifier = Modifier.padding(horizontal = 18.dp),
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
            }

            // Masonry grid — the only scrolling region of this screen
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
            ) {
                items(12) { idx ->
                    GifCard(
                        animationHeightDp = masonryHeights[idx % masonryHeights.size].dp,
                        onClick = { sheetVisible = true },
                    )
                }
            }
        }

        // Bottom sheet — slides up and fades in over a dim scrim
        AnimatedVisibility(
            visible = sheetVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        ) {
            ActionSheet(
                onDismiss = { sheetVisible = false },
                onAction = { sheetVisible = false },
            )
        }
    }
}
