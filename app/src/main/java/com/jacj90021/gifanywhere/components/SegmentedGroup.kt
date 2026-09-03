package com.jacj90021.gifanywhere.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.Extra
import com.jacj90021.gifanywhere.theme.InkBlack

@Composable
fun SegmentedGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(InkBlack.copy(alpha = 0.06f))
            .padding(4.dp)
            .border(2.dp, InkBlack, RoundedCornerShape(12.dp)),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .background(
                        if (isSelected) CardWhite else Color.Transparent,
                        RoundedCornerShape(8.dp),
                    )
                    .border(
                        if (isSelected) 2.dp else 0.dp,
                        InkBlack,
                        RoundedCornerShape(8.dp),
                    )
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.uppercase(),
                    style = Extra,
                    color = if (isSelected) InkBlack else InkBlack.copy(alpha = 0.65f),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                )
            }
        }
    }
}
