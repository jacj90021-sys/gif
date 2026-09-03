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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.MonoLabel
import com.jacj90021.gifanywhere.theme.MonoPill
import com.jacj90021.gifanywhere.theme.MonoValMuted
import com.jacj90021.gifanywhere.theme.RadiusLg
import com.jacj90021.gifanywhere.theme.Sub
import com.jacj90021.gifanywhere.theme.Title
import com.jacj90021.gifanywhere.theme.hardShadow

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
            .hardShadow(RadiusLg)
            .border(2.dp, InkBlack, RadiusLg),
        color = CardWhite,
        shape = RadiusLg,
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
    divider: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (divider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 14.dp)
                    .background(InkBlack.copy(alpha = 0.15f)),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = item.title,
                    style = Title,
                    color = InkBlack,
                )
                if (item.sub != null) {
                    Text(
                        text = item.sub,
                        style = Sub,
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
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .border(1.5.dp, InkBlack, RoundedCornerShape(12.dp)),
                    ) {
                        Text(
                            text = item.statusText,
                            style = MonoPill,
                            color = if (item.statusActive) InkBlack else InkMuted,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        )
                    }
                }
                item.valueText != null -> {
                    Text(
                        text = item.valueText,
                        style = MonoValMuted,
                        color = InkMuted,
                        modifier = Modifier.padding(start = 8.dp),
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
}

@Composable
private fun ToggleView(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 20.dp)
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
        style = MonoLabel,
        color = InkMuted,
        modifier = modifier.padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 6.dp),
    )
}

@Composable
fun ExportDefaultsRow(
    title: String,
    value: String,
    divider: Boolean = false,
    modifier: Modifier = Modifier,
) {
    SetRow(
        item = SetRowItem(title = title, valueText = value),
        divider = divider,
        modifier = modifier,
    )
}

@Composable
fun AboutRow(
    title: String,
    divider: Boolean = false,
    modifier: Modifier = Modifier,
) {
    SetRow(
        item = SetRowItem(title = title, valueText = "›"),
        divider = divider,
        modifier = modifier,
    )
}
