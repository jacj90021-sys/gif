package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Companion
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.RadiusLg
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Typography
import kotlin.math.roundToInt

data class FolderItem(
    val name: String,
    val count: String,
)

data class WallItem(
    val label: String = "SET",
)

@Composable
fun FolderGrid(
    items: List<FolderItem>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items.chunked(2).flatMap { row -> row }.forEachIndexed { idx, item ->
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    FolderCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun FolderCard(item: FolderItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RadiusMd)
            .background(CardWhite)
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .background(BgYellow, RadiusSm)
                    .border(1.5.dp, InkBlack, RadiusSm)
                    .padding(horizontal = 5.dp, vertical = 1.dp),
            ) {
                Text(
                    text = item.count,
                    style = Typography.bodySmall,
                    color = InkBlack,
                )
            }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = item.name,
                    style = Typography.labelMedium,
                    color = InkBlack,
                )
            }
        }
    }
}

@Composable
fun WallGrid(
    items: List<WallItem>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
    ) {
        itemsIndexed(items) { _, item ->
            Box(
                modifier = Modifier
                    .aspectRatio(9f / 16f)
                    .clip(RadiusSm)
                    .background(CardWhite)
                    .border(2.dp, InkBlack, RadiusSm),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(BgYellow, RadiusSm)
                        .border(1.5.dp, InkBlack, RadiusSm),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = item.label,
                        style = Typography.bodySmall,
                        color = InkBlack,
                    )
                }
            }
        }
    }
}

@Composable
fun CacheCard(
    cacheMegabytes: Int,
    cachePercent: Float,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Cache Storage",
                style = Typography.labelMedium,
                color = InkBlack,
            )
            Text(
                text = "$cacheMegabytes MB",
                style = Typography.labelSmall,
                color = InkBlack,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RadiusSm)
                .background(InkBlack.copy(alpha = 0.08f))
                .border(2.dp, InkBlack, RadiusSm),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(cachePercent.coerceIn(0f, 1f))
                    .background(BgYellow),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RadiusSm)
                .background(CardWhite)
                .border(2.dp, InkBlack, RadiusSm)
                .clickable { onClear() }
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "CLEAR CACHE",
                style = Typography.labelMedium,
                color = InkBlack,
            )
        }
    }
}
