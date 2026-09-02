package com.jacj90021.gifanywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jacj90021.gifanywhere.navigation.AppNavigation
import com.jacj90021.gifanywhere.theme.GifAnywhereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GifAnywhereTheme {
                AppNavigation()
            }
        }
    }
}
