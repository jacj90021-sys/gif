package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.Bold
import com.jacj90021.gifanywhere.theme.MonoSize
import com.jacj90021.gifanywhere.theme.MonoTag
import com.jacj90021.gifanywhere.theme.RadiusLg
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Title
import com.jacj90021.gifanywhere.theme.Wall

@Composable
fun BoxCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .border(2.dp, InkBlack, RadiusLg),
        color = CardWhite,
        shape = RadiusLg,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
        ) {
            content()
        }
    }
}

data class FolderItem(
    val name: String,
    val count: String,
)

@Composable
fun FolderGrid(
    items: List<FolderItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        FolderCard(item = item)
                    }
                }
                repeat(2 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
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
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
        shape = RadiusMd,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(BgYellow, RadiusSm)
                    .border(1.5.dp, InkBlack, RadiusSm)
                    .padding(horizontal = 5.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.count,
                    style = MonoTag,
                    color = InkBlack,
                )
            }
            Text(
                text = item.name,
                style = Title,
                color = InkBlack,
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}

data class WallItem(
    val label: String = "SET",
)

@Composable
fun WallGrid(
    items: List<WallItem>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.chunked(3).forEach { rowItems ->
            rowItems.forEach { item ->
                Box(modifier = Modifier.weight(1f)) {
                    WallCell(item = item)
                }
            }
            repeat(3 - rowItems.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun WallCell(item: WallItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .background(CardWhite, RadiusSm)
            .border(2.dp, InkBlack, RadiusSm),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(4.dp)
                .background(BgYellow, RoundedCornerShape(4.dp))
                .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = item.label,
                style = Wall,
                color = InkBlack,
                modifier = Modifier.padding(vertical = 2.dp),
            )
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
    val fraction = cachePercent.coerceIn(0f, 1f)
    BoxCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Cache Storage",
                style = Title,
                color = InkBlack,
            )
            Text(
                text = "$cacheMegabytes MB",
                style = MonoSize,
                color = InkBlack,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(InkBlack.copy(alpha = 0.08f))
                .border(2.dp, InkBlack, RoundedCornerShape(3.dp)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
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
                style = Bold,
                color = InkBlack,
            )
        }
    }
}
