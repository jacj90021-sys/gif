package com.jacj90021.gifanywhere

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.nav.AppNav
import com.jacj90021.gifanywhere.ui.theme.GifAnywhereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!Store.onboardingDone) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        val startRoute = intent?.getStringExtra("route")?.takeIf { it.isNotBlank() } ?: "discover"
        setContent {
            GifAnywhereTheme {
                AppNav(startRoute = startRoute)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Store.save(this)
    }
}
