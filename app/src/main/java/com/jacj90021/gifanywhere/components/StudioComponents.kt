package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.Bold
import com.jacj90021.gifanywhere.theme.Extra
import com.jacj90021.gifanywhere.theme.MonoVal
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusPlatform
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Slider
import com.jacj90021.gifanywhere.theme.Sub
import com.jacj90021.gifanywhere.theme.Title
import com.jacj90021.gifanywhere.theme.hardShadow

@Composable
fun SingleToggle(
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = 38.dp, height = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (enabled) BgYellow else InkBlack.copy(alpha = 0.1f))
            .border(2.dp, InkBlack, RoundedCornerShape(10.dp))
            .clickable { onToggle() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .offset(x = if (enabled) 19.dp else 1.dp)
                .size(14.dp, 14.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(InkBlack),
        )
    }
}

data class SourceItem(
    val iconRes: Int,
    val label: String,
)

data class ToolItem(
    val iconRes: Int,
    val name: String,
    val sub: String = "",
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
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
    ) {
        itemsIndexed(items) { _, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { },
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .hardShadow(RadiusMd)
                        .background(CardWhite, RadiusMd)
                        .border(2.dp, InkBlack, RadiusMd),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = null,
                        tint = InkBlack,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = item.label,
                    style = Bold,
                    color = InkBlack,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

/**
 * Tools grid matching the mockup: the big tool spans a full row,
 * every following pair of tools shares one row (2 columns).
 */
@Composable
fun ToolsGrid(
    items: List<ToolItem>,
    modifier: Modifier = Modifier,
) {
    val big = items.find { it.isBig }
    val small = items.filter { !it.isBig }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        big?.let { ToolCardBig(item = it) }
        small.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        ToolCardSmall(item = item)
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
private fun ToolCardBig(item: ToolItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(RadiusMd)
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
        shape = RadiusMd,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(BgYellow, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = null,
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = item.name,
                    style = Title,
                    color = InkBlack,
                )
                if (item.sub.isNotEmpty()) {
                    Text(
                        text = item.sub,
                        style = Sub,
                        color = InkMuted,
                        modifier = Modifier.padding(top = 1.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolCardSmall(item: ToolItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(RadiusMd)
            .border(2.dp, InkBlack, RadiusMd),
        color = CardWhite,
        shape = RadiusMd,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(BgYellow, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = null,
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = item.name,
                style = Title,
                color = InkBlack,
                modifier = Modifier.padding(top = 6.dp),
            )
            if (item.sub.isNotEmpty()) {
                Text(
                    text = item.sub,
                    style = Sub,
                    color = InkMuted,
                    modifier = Modifier.padding(top = 1.dp),
                )
            }
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
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .background(if (isSelected) InkBlack else CardWhite, RadiusSm)
                    .border(2.dp, InkBlack, RadiusSm)
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    style = Extra,
                    color = if (isSelected) CardWhite else InkBlack,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 6.dp),
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
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
    ) {
        itemsIndexed(options) { index, option ->
            val isSelected = index in selectedIndices
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
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
                    style = Bold,
                    color = InkBlack,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
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
    val fraction = fillPercent.coerceIn(0f, 1f)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = Slider,
                color = InkBlack,
            )
            Text(
                text = value,
                style = MonoVal,
                color = InkBlack,
                modifier = Modifier
                    .background(BgYellow, RoundedCornerShape(4.dp))
                    .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 1.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Canvas area tall enough for the handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
        ) {
            // Track
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(InkBlack.copy(alpha = 0.08f))
                    .border(2.dp, InkBlack, RoundedCornerShape(3.dp)),
            ) {
                // Yellow fill
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                        .background(BgYellow),
                )
            }
            // Handle — sits at the fraction point
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(fraction))
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .offset(x = (-7).dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(CardWhite)
                        .border(2.dp, InkBlack, RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(InkBlack),
                    )
                }
                Spacer(modifier = Modifier.weight(1f - fraction))
            }
        }
    }
}
