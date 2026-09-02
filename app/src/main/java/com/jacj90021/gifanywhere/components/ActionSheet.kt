package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.VectorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.TextAlign
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.RadiusMd
import com.jacj90021.gifanywhere.theme.RadiusSheet
import com.jacj90021.gifanywhere.theme.Typography

data class SheetAction(
    val label: String,
    val pathData: String,
    val isPrimary: Boolean = false,
)

val sheetActions = listOf(
    SheetAction(
        label = "Send",
        pathData = "M22 2 L11 13 M22 2 l-7 20 l-4 -9 l-9 -4 l20 -7",
        isPrimary = true,
    ),
    SheetAction(
        label = "Save",
        pathData = "M19 21 l-7 -5 l-7 5 l0 -1 V5 a2 2 0 0 1 2 -2 l10 0 a2 2 0 0 1 2 2 z",
    ),
    SheetAction(
        label = "Favorite",
        pathData = "M20.8 4.6 a5.5 5.5 0 0 0 -7.8 0 l-1 -1 a5.5 5.5 0 0 0 -7.8 7.8 l1 1 L12 21 l7.8 -7.8 l1 -1 a5.5 5.5 0 0 0 0 -7.8",
    ),
    SheetAction(
        label = "Edit",
        pathData = "M12 20 l9 0 M16.5 3.5 a2.1 2.1 0 0 1 3 3 l-9 16 l-4 1 l1 -4",
    ),
    SheetAction(
        label = "Convert",
        pathData = "M17 2 l4 4 l-4 4 M3 11 V9 a4 4 0 0 1 4 -4 l14 0 M7 22 l-4 -4 l4 -4 M21 13 v2 a4 4 0 0 1 -4 4 l-14 0",
    ),
    SheetAction(
        label = "Wallpaper",
        pathData = "M3 3 l18 0 l0 18 l-18 0 z M8.5 8.5 m-1.5 0 a1.5 1.5 0 1 1 3 0 a1.5 1.5 0 1 1 -3 0 M21 15 l-5 -5 l-5 5",
    ),
)

fun buildSheetIcon(pathData: String): ImageVector = ImageVector.Builder(
    name = "SheetIcon",
    defaultWidth = 24f,
    defaultHeight = 24f,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    addPath(
        fill = null,
        stroke = Color.Black,
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        path = Path().apply { this@apply.moveTo(0f, 0f) }.apply {
            // Parse path commands manually
            parsePath(pathData)
        },
    )
}.build()

private fun Path.parsePath(data: String) {
    var i = 0
    val chars = data.toCharArray()
    var currentX = 0f
    var currentY = 0f
    val firstX = 0f
    val firstY = 0f
    fun nextNumber(start: Int): Pair<Float, Int> {
        var j = start
        while (j < chars.size && (chars[j].isDigit() || chars[j] == '-' || chars[j] == '.' || chars[j] == 'e' || chars[j] == 'E')) {
            j++
        }
        val num = chars.sliceArray(start until j).toString().toFloatOrNull() ?: 0f
        return num to j
    }
    while (i < chars.size) {
        val c = chars[i]
        if (c == ' ') { i++; continue }
        if (c == 'M') {
            i++
            val (nx, nj) = nextNumber(i)
            i = nj
            val (ny, nj2) = nextNumber(i)
            i = nj2
            currentX = nx
            currentY = ny
            this.moveTo(currentX, currentY)
            firstX = currentX
            firstY = currentY
        } else if (c == 'L') {
            i++
            val (nx, nj) = nextNumber(i)
            i = nj
            val (ny, nj2) = nextNumber(i)
            i = nj2
            this.lineTo(currentX + nx, currentY + ny)
            currentX += nx
            currentY += ny
        } else if (c == 'l') {
            i++
            val (nx, nj) = nextNumber(i)
            i = nj
            val (ny, nj2) = nextNumber(i)
            i = nj2
            this.lineTo(currentX + nx, currentY + ny)
            currentX += nx
            currentY += ny
        } else if (c == 'H') {
            i++
            val (nx, nj) = nextNumber(i)
            i = nj
            this.lineTo(currentX + nx, currentY)
            currentX += nx
        } else if (c == 'h') {
            i++
            val (nx, nj) = nextNumber(i)
            i = nj
            this.lineTo(currentX + nx, currentY)
            currentX += nx
        } else if (c == 'V') {
            i++
            val (ny, nj) = nextNumber(i)
            i = nj
            this.lineTo(currentX, currentY + ny)
            currentY += ny
        } else if (c == 'v') {
            i++
            val (ny, nj) = nextNumber(i)
            i = nj
            this.lineTo(currentX, currentY + ny)
            currentY += ny
        } else if (c == 'a') {
            i++
            // ellipse arc: rx ry x-axis-rotation large-arc sweep dx dy
            val params = (1..6).fold(i to listOf<Float>()) { (idx, acc) ->
                val (n, nj) = nextNumber(idx)
                idx to acc + n
            }
            // skip for simplicity: treat as line
            i = params.first
        } else if (c == 'A') {
            i++
            i = i + 6 // skip arc params
        } else if (c == 'q') {
            i++
            val (cx, nj) = nextNumber(i)
            i = nj
            val (cy, nj2) = nextNumber(i)
            i = nj2
            val (ex, nj3) = nextNumber(i)
            i = nj3
            val (ey, nj4) = nextNumber(i)
            i = nj4
            this.quadTo(currentX + cx, currentY + cy, currentX + ex, currentY + ey)
            currentX += ex
            currentY += ey
        } else if (c == 't') {
            i++
            // smooth quad, use previous control
            i = i
        } else if (c == 'c') {
            i++
            i = i + 6
        } else if (c == 's') {
            i++
            i = i + 4
        } else if (c == 'Z' || c == 'z') {
            i++
            this.close()
            currentX = firstX
            currentY = firstY
        } else {
            i++
        }
    }
}

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
                        .opacity(0.3f),
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
                                            imageVector = buildSheetIcon(action.pathData),
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
