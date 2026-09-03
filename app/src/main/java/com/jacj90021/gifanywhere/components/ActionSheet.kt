package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSheet
import com.jacj90021.gifanywhere.theme.Sheet

data class SheetAction(
    val label: String,
    val iconRes: Int,
    val isPrimary: Boolean = false,
)

val sheetActions = listOf(
    SheetAction("Send", AppIcons.Send, isPrimary = true),
    SheetAction("Save", AppIcons.Save),
    SheetAction("Favorite", AppIcons.Favorite),
    SheetAction("Edit", AppIcons.Edit),
    SheetAction("Convert", AppIcons.Convert),
    SheetAction("Wallpaper", AppIcons.Wallpaper),
)

@Composable
fun ActionSheet(
    onDismiss: () -> Unit,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Dim backdrop — tapping outside dismisses
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        )
        // Bottom sheet card
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .border(2.dp, InkBlack, RadiusSheet),
            color = CardWhite,
            shape = RadiusSheet,
        ) {
            Column(
                modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(32.dp)
                        .height(4.dp)
                        .background(InkBlack.copy(alpha = 0.3f), RoundedCornerShape(2.dp)),
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Preview placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(BgYellow)
                        .border(2.dp, InkBlack, RadiusMd),
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 3-across action grid
                sheetActions.chunked(3).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        rowActions.forEach { action ->
                            Box(modifier = Modifier.weight(1f)) {
                                SheetActionCell(
                                    action = action,
                                    onClick = { onAction(action.label) },
                                )
                            }
                        }
                        repeat(3 - rowActions.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun SheetActionCell(
    action: SheetAction,
    onClick: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (action.isPrimary) BgYellow else CardWhite,
                    RadiusMd,
                )
                .border(2.dp, InkBlack, RadiusMd)
                .clickable {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(action.iconRes),
                contentDescription = null,
                tint = InkBlack,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(
            text = action.label.uppercase(),
            style = Sheet,
            color = InkBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
