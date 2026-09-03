package com.jacj90021.gifanywhere.components

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.AppIcons
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Typography

private data class TabItem(
    val id: String,
    val label: String,
    val iconRes: Int,
)

private val tabs = listOf(
    TabItem("discover", "DISCOVER", AppIcons.Discover),
    TabItem("studio", "STUDIO", AppIcons.Studio),
    TabItem("library", "LIBRARY", AppIcons.Library),
    TabItem("settings", "SETTINGS", AppIcons.Settings),
)

@Composable
fun NavBar(
    activeId: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardWhite)
            .navigationBarsPadding(),
    ) {
        // 2px ink top border, like the mockup navbar
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(InkBlack),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val haptics = LocalHapticFeedback.current
            tabs.forEach { tab ->
                val isActive = tab.id == activeId
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onTabSelected(tab.id)
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                if (isActive) BgYellow else Color.Transparent,
                                RadiusSm,
                            )
                            .border(
                                if (isActive) 2.dp else 0.dp,
                                InkBlack,
                                RadiusSm,
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(tab.iconRes),
                            contentDescription = null,
                            tint = if (isActive) InkBlack else InkMuted,
                            modifier = Modifier.size(17.dp),
                        )
                    }
                    Text(
                        text = tab.label,
                        style = Typography.labelSmall,
                        color = if (isActive) InkBlack else InkMuted,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
            }
        }
    }
}
