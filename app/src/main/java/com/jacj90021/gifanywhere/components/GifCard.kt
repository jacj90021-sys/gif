package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.MonoBadge
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSm

@Composable
fun GifCard(
    animationHeightDp: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RadiusMd)
            .background(CardWhite)
            .border(2.dp, InkBlack, RadiusMd)
            .clickable {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
    ) {
        // Media area — placeholder until real GIF sources are wired in
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animationHeightDp)
                .background(Color(0xFFF7F6F1)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(AppIcons.Wallpaper),
                contentDescription = null,
                tint = InkBlack.copy(alpha = 0.22f),
                modifier = Modifier.size(26.dp),
            )
        }
        // Loop badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .background(CardWhite, RadiusSm)
                .border(2.dp, InkBlack, RadiusSm)
                .padding(horizontal = 5.dp, vertical = 2.dp),
        ) {
            Text(
                text = "↻ LOOP",
                style = MonoBadge,
                color = InkBlack,
            )
        }
    }
}
