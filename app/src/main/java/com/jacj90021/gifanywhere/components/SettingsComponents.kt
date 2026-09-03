package com.jacj90021.gifanywhere.components
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.jacj90021.gifanywhere.theme.RadiusLg
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Typography

data class SetRowItem(
    val title: String,
    val sub: String? = null,
    val statusText: String? = null,
    val statusActive: Boolean = false,
    val hasToggle: Boolean = false,
    val toggleOn: Boolean = false,
    val valueText: String? = null,
)

@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RadiusLg)
            .background(CardWhite)
            .border(2.dp, InkBlack, RadiusLg),
        color = CardWhite,
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun SetRow(
    item: SetRowItem,
    onToggle: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp, 14.dp)
            .background(
                if (item.hasToggle) Color.Transparent else Color.Transparent,
                RoundedCornerShape(0.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.title,
                style = Typography.labelMedium,
                color = InkBlack,
            )
            if (item.sub != null) {
                Text(
                    text = item.sub,
                    style = Typography.bodySmall,
                    color = InkMuted,
                    modifier = Modifier.padding(top = 1.dp),
                )
            }
        }
        when {
            item.statusText != null -> {
                Surface(
                    color = if (item.statusActive) BgYellow else CardWhite,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Text(
                        text = item.statusText,
                        style = Typography.labelSmall,
                        color = if (item.statusActive) InkBlack else InkMuted,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    )
                }
            }
            item.valueText != null -> {
                Text(
                    text = item.valueText,
                    style = Typography.labelSmall,
                    color = InkMuted,
                )
            }
            item.hasToggle -> {
                Box(
                    modifier = Modifier.padding(start = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    ToggleView(
                        enabled = item.toggleOn,
                        onClick = onToggle,
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleView(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(38.dp, 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (enabled) BgYellow else InkBlack.copy(alpha = 0.1f))
            .border(2.dp, InkBlack, RoundedCornerShape(10.dp))
            .clickable { onClick() },
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

@Composable
fun CardLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = Typography.bodySmall,
        color = InkMuted,
        modifier = modifier.padding(horizontal = 18.dp, vertical = 4.dp),
    )
}

@Composable
fun ExportDefaultsRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    SetRow(
        item = SetRowItem(title = title, valueText = value),
        modifier = modifier,
    )
}

@Composable
fun AboutRow(
    title: String,
    modifier: Modifier = Modifier,
) {
    SetRow(
        item = SetRowItem(title = title, valueText = "›"),
        modifier = modifier,
    )
}
