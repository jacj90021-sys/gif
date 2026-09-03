package com.jacj90021.gifanywhere.components
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.Typography

@Composable
fun TopBar(
    title: String,
    tag: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$title.",
            style = Typography.labelLarge,
            color = InkBlack,
            modifier = Modifier.padding(end = 6.dp),
        )
        if (tag != null) {
            Box(
                modifier = Modifier
                    .background(InkBlack, shape = com.jacj90021.gifanywhere.theme.RadiusSmall)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
                    .size(width = 30.dp, height = 18.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tag,
                    style = Typography.bodySmall,
                    color = BgYellow,
                )
            }
        }
    }
}
