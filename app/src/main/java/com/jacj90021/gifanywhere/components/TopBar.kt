package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.Headline
import com.jacj90021.gifanywhere.theme.HeadlineSm
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.MonoTag

@Composable
fun TopBar(
    title: String,
    tag: String? = null,
    fontSizeSp: Int = 24,
    modifier: Modifier = Modifier,
) {
    val titleStyle = when (fontSizeSp) {
        18 -> HeadlineSm
        else -> Headline
    }
    Row(
        modifier = modifier.padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$title.",
            style = titleStyle,
            color = InkBlack,
        )
        if (tag != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(InkBlack, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tag,
                    style = MonoTag.copy(fontSize = 10.sp),
                    color = BgYellow,
                    maxLines = 1,
                )
            }
        }
    }
}
