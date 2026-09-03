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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacj90021.gifanywhere.R
import com.jacj90021.gifanywhere.theme.BgYellow
import com.jacj90021.gifanywhere.theme.InkBlack
import com.jacj90021.gifanywhere.theme.MonoTag

@Composable
fun TopBar(
    title: String,
    tag: String? = null,
    fontSizeSp: Int = 24,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$title.",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans, FontWeight.ExtraBold)),
                fontSize = fontSizeSp.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
            ),
            color = InkBlack,
        )
        if (tag != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(InkBlack, RoundedCornerShape(8.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tag,
                    style = MonoTag,
                    color = BgYellow,
                )
            }
        }
    }
}
