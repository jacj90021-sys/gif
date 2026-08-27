package com.jacj90021.gifanywhere

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.nav.AppNav
import com.jacj90021.gifanywhere.ui.theme.GifAnywhereTheme

class MainActivity : ComponentActivity() {

    /** Route requested by the Floating Bubble petals ("studio", "tool/editor", …). */
    private var routeRequest by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Classic professional layout: the app sits BETWEEN the system bars —
        // the status bar stays an opaque system-owned strip (theme: black),
        // the nav bar a system-owned strip (theme: charcoal). The app does
        // NOT draw edge-to-edge over them. The Compose inset padding in the
        // UI stays as a safety net; it no-ops when the window isn't fullbleed.
        // Launch onboarding only on a FRESH creation — never on rotation
        // (which recreates the activity and used to stack a duplicate intent),
        // and never once it has been completed.
        if (!Store.onboardingDone && savedInstanceState == null) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
        routeRequest = intent?.getStringExtra("route")?.takeIf { it.isNotBlank() }
        setContent {
            GifAnywhereTheme {
                AppNav(routeRequest = routeRequest)
            }
        }
    }

    // The bubble opens the app while it is already running: with
    // launchMode="singleTask" the intent is delivered here instead of
    // stacking a second MainActivity that would ignore the route.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.getStringExtra("route")?.takeIf { it.isNotBlank() }?.let { routeRequest = it }
    }

    override fun onPause() {
        super.onPause()
        Store.save(this)
    }
}
