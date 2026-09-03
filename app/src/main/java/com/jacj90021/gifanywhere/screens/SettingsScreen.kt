package com.jacj90021.gifanywhere.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacj90021.gifanywhere.components.AboutRow
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
        ) {
            CardLabel("DELIVERY")
            SettingsGroup(modifier = Modifier.padding(horizontal = 18.dp)) {
                SetRow(
                    item = SetRowItem(
                        title = "Keyboard",
                        sub = "System-wide GIF keyboard",
                        statusText = "NOT ENABLED",
                        statusActive = false,
                    ),
                )
                SetRow(
                    item = SetRowItem(
                        title = "Floating Bubble",
                        sub = "Quick access overlay",
                        hasToggle = true,
                        toggleOn = true,
                    ),
                    divider = true,
                    onToggle = {},
                )
            }

            CardLabel("EXPORT DEFAULTS")
            SettingsGroup(modifier = Modifier.padding(horizontal = 18.dp)) {
                ExportDefaultsRow(title = "Default Format", value = "Automatic ›")
                ExportDefaultsRow(title = "Quality", value = "Auto ›", divider = true)
            }

            CardLabel("GENERAL")
            SettingsGroup(modifier = Modifier.padding(horizontal = 18.dp)) {
                ExportDefaultsRow(title = "Theme", value = "Light ›")
                ExportDefaultsRow(title = "Language", value = "English ›", divider = true)
                AboutRow(title = "About / Feedback", divider = true)
            }
        }
    }
}
