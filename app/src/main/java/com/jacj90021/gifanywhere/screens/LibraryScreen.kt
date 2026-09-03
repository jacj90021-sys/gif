package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.CacheCard
import com.jacj90021.gifanywhere.components.FolderGrid
import com.jacj90021.gifanywhere.components.FolderItem
import com.jacj90021.gifanywhere.components.SegmentedGroup
import com.jacj90021.gifanywhere.components.TopBar
import com.jacj90021.gifanywhere.components.WallGrid
import com.jacj90021.gifanywhere.components.WallItem

@Composable
fun LibraryScreen(
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedFolderIndex by remember { mutableIntStateOf(0) }
    var selectedWallIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 8.dp),
        ) {
            TopBar(title = "LIBRARY")
        }
        Spacer(modifier = Modifier.height(6.dp))
        SegmentedGroup(
            options = listOf("Favorites", "Recent", "Creations"),
            selectedIndex = selectedFolderIndex,
            onSelected = { selectedFolderIndex = it },
            modifier = Modifier.padding(horizontal = 18.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
        ) {
            FolderGrid(
                items = listOf(
                    FolderItem("Reactions", "42"),
                    FolderItem("Work Chat", "18"),
                    FolderItem("Sticker Packs", "7"),
                    FolderItem("New Folder", "+"),
                ),
                modifier = Modifier.padding(top = 10.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            TopBar(title = "WALLPAPERS", fontSizeSp = 18)
            Spacer(modifier = Modifier.height(6.dp))
            SegmentedGroup(
                options = listOf("Home", "Lock", "Both"),
                selectedIndex = selectedWallIndex,
                onSelected = { selectedWallIndex = it },
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            WallGrid(items = List(3) { WallItem() })
            Spacer(modifier = Modifier.height(4.dp))
            CacheCard(
                cacheMegabytes = 380,
                cachePercent = 0.38f,
                onClear = {},
            )
        }
    }
}
