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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Companion
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusPlatform
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Typography
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection

data class SourceItem(
    val label: String,
    val iconPath: String,
)

data class ToolItem(
    val name: String,
    val sub: String,
    val iconPath: String,
    val isBig: Boolean = false,
)

@Composable
fun SourceRow(
    items: List<SourceItem>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
    ) {
        itemsIndexed(items) { _, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { },
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(CardWhite, RadiusMd)
                        .border(2.dp, InkBlack, RadiusMd),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = buildSheetIcon(item.iconPath),
                        contentDescription = null,
                        tint = InkBlack,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = item.label,
                    style = Typography.labelMedium,
                    color = InkBlack,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
fun ToolsGrid(
    items: List<ToolItem>,
    modifier: Modifier = Modifier,
) {
    val big = items.find { it.isBig }
    val small = items.filter { !it.isBig }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (big != null) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                ToolCardBig(item = big)
            }
        }
        small.forEach { item ->
            Box(
                modifier = Modifier.weight(1f),
            ) {
                ToolCardSmall(item = item)
            }
        }
    }
}

@Composable
private fun ToolCardBig(item: ToolItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RadiusMd)
            .background(CardWhite)
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(BgYellow, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = buildSheetIcon(item.iconPath),
                    contentDescription = null,
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = item.name,
                    style = Typography.labelMedium,
                    color = InkBlack,
                )
                Text(
                    text = item.sub,
                    style = Typography.bodySmall,
                    color = InkMuted,
                    modifier = Modifier.padding(top = 1.dp),
                )
            }
        }
    }
}

@Composable
private fun ToolCardSmall(item: ToolItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RadiusMd)
            .background(CardWhite)
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(BgYellow, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = buildSheetIcon(item.iconPath),
                    contentDescription = null,
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = item.name,
                style = Typography.labelMedium,
                color = InkBlack,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
fun FormatChips(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 7.dp)
                    .background(if (isSelected) InkBlack else CardWhite, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm)
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    style = Typography.labelMedium,
                    color = if (isSelected) CardWhite else InkBlack,
                )
            }
        }
    }
}

@Composable
fun PlatformChips(
    options: List<String>,
    selectedIndices: Set<Int>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        itemsIndexed(options) { index, option ->
            val isSelected = index in selectedIndices
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .background(
                        if (isSelected) BgYellow else CardWhite,
                        RadiusPlatform,
                    )
                    .border(2.dp, InkBlack, RadiusPlatform)
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    style = Typography.labelMedium,
                    color = InkBlack,
                )
            }
        }
    }
}

@Composable
fun SliderRow(
    label: String,
    value: String,
    fillPercent: Float,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = Typography.bodyMedium,
                color = InkBlack,
            )
            Text(
                text = value,
                style = Typography.labelSmall,
                color = InkBlack,
                modifier = Modifier
                    .background(BgYellow, RoundedCornerShape(4.dp))
                    .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 1.dp),
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
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
                    .fillMaxWidth(fillPercent.coerceIn(0f, 1f))
                    .background(BgYellow),
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        Box(
            modifier = Modifier
                .offset { IntOffset((fillPercent.coerceIn(0f, 1f) * 375).toInt(), -3) }
                .size(14.dp)
                .background(CardWhite, RoundedCornerShape(14.dp))
                .border(2.dp, InkBlack, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(modifier = Modifier.size(4.dp).background(InkBlack))
        }
    }
}
