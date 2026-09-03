package com.jacj90021.gifanywhere.components
import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.CardWhite
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.Typography

@Composable
fun ChipRow(
    options: List<String>,
    selectedIndices: Set<Int>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
    ) {
        itemsIndexed(options) { index, option ->
            val isSelected = index in selectedIndices
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .width(80.dp)
                    .background(
                        if (isSelected) InkBlack else CardWhite,
                        RoundedCornerShape(20.dp),
                    )
                    .border(2.dp, InkBlack, RoundedCornerShape(20.dp))
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option,
                    style = Typography.labelMedium,
                    color = if (isSelected) CardWhite else InkBlack,
                )
            }
        }
    }
}
