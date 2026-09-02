package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.CardLabel
import com.jacj90021.gifanywhere.components.ExportDefaultsRow
import com.jacj90021.gifanywhere.components.SettingsGroup
import com.jacj90021.gifanywhere.components.SetRow
import com.jacj90021.gifanywhere.components.SetRowItem
import com.jacj90021.gifanywhere.components.StatusBar
import com.jacj90021.gifanywhere.components.TopBar

@Composable
fun SettingsScreen(
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        StatusBar()
        Spacer(modifier = Modifier.height(4.dp))
        TopBar(title = "SETTINGS")
        Spacer(modifier = Modifier.height(4.dp))

        CardLabel("Delivery", modifier = Modifier)
        Spacer(modifier = Modifier.height(0.dp))
        SettingsGroup(modifier = Modifier.padding(top = 0.dp)) {
            SetRow(
                item = SetRowItem(
                    title = "Keyboard",
                    sub = "System-wide GIF keyboard",
                    statusText = "NOT ENABLED",
                    statusActive = false,
                ),
                modifier = Modifier.padding(top = 0.dp),
            )
            SetRow(
                item = SetRowItem(
                    title = "Floating Bubble",
                    sub = "Quick access overlay",
                    hasToggle = true,
                    toggleOn = true,
                    onToggle = {},
                ),
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        CardLabel("Export Defaults", modifier = Modifier)
        Spacer(modifier = Modifier.height(0.dp))
        SettingsGroup(modifier = Modifier.padding(top = 0.dp)) {
            ExportDefaultsRow(
                title = "Default Format",
                value = "Automatic ›",
                modifier = Modifier.padding(top = 0.dp),
            )
            ExportDefaultsRow(
                title = "Quality",
                value = "Auto ›",
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        CardLabel("General", modifier = Modifier)
        Spacer(modifier = Modifier.height(0.dp))
        SettingsGroup(modifier = Modifier.padding(top = 0.dp)) {
            ExportDefaultsRow(
                title = "Theme",
                value = "Light ›",
                modifier = Modifier.padding(top = 0.dp),
            )
            ExportDefaultsRow(
                title = "Language",
                value = "English ›",
            )
            ExportDefaultsRow(
                title = "About / Feedback",
                value = "›",
            )
        }
    }
}
