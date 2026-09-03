package com.jacj90021.gifanywhere.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Crisp, flat offset shadow — the mockup's `box-shadow: 3px 3px 0`.
 * Draws a solid shape shifted down-right behind the element.
 * Place it BEFORE the element's background/border modifiers.
 */
fun Modifier.hardShadow(
    shape: Shape = RoundedCornerShape(12.dp),
    offset: Dp = 3.dp,
    color: Color = InkBlack,
): Modifier = drawBehind {
    val o = offset.toPx()
    val outline = shape.createOutline(size, layoutDirection, this)
    when (outline) {
        is Outline.Rounded -> {
            val rr = outline.roundRect
            drawRoundRect(
                color = color,
                topLeft = Offset(o, o),
                size = size,
                cornerRadius = CornerRadius(
                    x = rr.topLeftCornerRadius.x,
                    y = rr.topLeftCornerRadius.y,
                ),
            )
        }
        else -> drawRect(
            color = color,
            topLeft = Offset(o, o),
            size = size,
        )
    }
}
