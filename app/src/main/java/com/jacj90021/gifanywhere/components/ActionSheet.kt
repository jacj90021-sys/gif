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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSheet
import com.jacj90021.gifanywhere.theme.Typography
import com.jacj90021.gifanywhere.components.AppIcons

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
    previewHeightDp: Dp = 120.dp,
    onDismiss: () -> Unit,
    onAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.65f),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(CardWhite, RadiusSheet)
                    .border(2.dp, InkBlack, RadiusSheet)
                    .padding(top = 12.dp, bottom = 20.dp, start = 18.dp, end = 18.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .background(InkBlack, RoundedCornerShape(2.dp))
                        .padding(top = 0.dp, bottom = 10.dp)
                        .graphicsLayer(
                            alpha = 0.3f
                        ),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(previewHeightDp)
                        .background(BgYellow)
                        .border(2.dp, InkBlack, RadiusMd),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    repeat(2) { colIndex ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f),
                        ) {
                            sheetActions.chunked(3).getOrElse(colIndex) { emptyList() }.forEach { action ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .size(44.dp)
                                        .background(
                                            if (action.isPrimary) BgYellow else CardWhite,
                                            RadiusMd,
                                        )
                                        .border(2.dp, InkBlack, RadiusMd)
                                        .clickable { onAction(action.label) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        Icon(
                                            painter = painterResource(action.iconRes),
                                            contentDescription = null,
                                            tint = InkBlack,
                                            modifier = Modifier.size(18.dp),
                                        )
                                        Text(
                                            text = action.label,
                                            style = Typography.bodySmall,
                                            color = InkBlack,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
