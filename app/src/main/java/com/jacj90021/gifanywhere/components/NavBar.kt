package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.VectorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathFillType
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.InkMuted
import com.jacj90021.gifanywhere.theme.RadiusSm
import com.jacj90021.gifanywhere.theme.Typography

private data class TabItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
)

private val tabs = listOf(
    TabItem(
        id = "discover",
        label = "DISCOVER",
        icon = IconPaths.Discover,
    ),
    TabItem(
        id = "studio",
        label = "STUDIO",
        icon = IconPaths.Studio,
    ),
    TabItem(
        id = "library",
        label = "LIBRARY",
        icon = IconPaths.Library,
    ),
    TabItem(
        id = "settings",
        label = "SETTINGS",
        icon = IconPaths.Settings,
    ),
)

@Composable
fun NavBar(
    activeId: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = CardWhite,
        shape = com.jacj90021.gifanywhere.theme.RadiusLg,
    ) {
        Box(modifier = Modifier.border(2.dp, InkBlack, com.jacj90021.gifanywhere.theme.RadiusLg)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    . Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { tab ->
                    val isActive = tab.id == activeId
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onTabSelected(tab.id) },
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
                                imageVector = tab.icon,
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
}

// SVG icon wrappers matching the HTML paths.
object IconPaths {
    val Discover: ImageVector
        get() = ImageVector.Builder(
            name = "Discover",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).addPath(
            fill = null,
            stroke = Color.Black,
            strokeLineWidth = 2.2f,
            strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Butt,
            strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Miter,
            pathData = Path.Builder().apply {
                moveTo(11f, 11f)
                arcTo(8f, 8f, 0f, false, true, 19f, 19f)
                moveTo(21f, 21f)
                lineTo(16.65f, 16.65f)
            }.build(),
        ).build()

    val Studio: ImageVector
        get() = ImageVector.Builder(
            name = "Studio",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(12f, 20f)
                    hLineTo(21f)
                }.build(),
            )
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(16.5f, 3.5f)
                    cubicTo(
                        17.71f, 4.71f, 18.5f, 5.5f, 18.5f, 6.79f,
                    )
                    lineTo(7f, 19f)
                    lineTo(3f, 20f)
                    lineTo(4f, 16f)
                }.build(),
            )
        }.build()

    val Library: ImageVector
        get() = ImageVector.Builder(
            name = "Library",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(3f, 4f)
                    lineTo(3f, 20f)
                    lineTo(21f, 20f)
                    lineTo(21f, 4f)
                    close()
                }.build(),
            )
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(8f, 2f)
                    lineTo(8f, 6f)
                    moveTo(16f, 2f)
                    lineTo(16f, 6f)
                    moveTo(3f, 10f)
                    lineTo(21f, 10f)
                }.build(),
            )
        }.build()

    val Settings: ImageVector
        get() = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(12f, 12f)
                    arcTo(3f, 3f, 0f, false, true, 15f, 12f)
                }.build(),
            )
            addPath(
                fill = null,
                stroke = Color.Black,
                strokeLineWidth = 2.2f,
                pathData = Path.Builder().apply {
                    moveTo(19.4f, 15f)
                    cubicTo(
                        19.73f, 16.82f, 20f, 17.08f, 20f, 17.5f,
                    )
                    lineTo(12f, 12f)
                }.build(),
            )
        }.build()
}
