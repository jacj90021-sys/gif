package com.jacj90021.gifanywhere

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jacj90021.gifanywhere.data.Store
import com.jacj90021.gifanywhere.ui.components.clickableNoRipple
import com.jacj90021.gifanywhere.ui.theme.*
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GifAnywhereTheme {
                OnboardingFlow(onDone = ::finishOnboarding)
            }
        }
    }

    private fun finishOnboarding() {
        Store.onboardingDone = true
        Store.save(this)
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }
}

private fun keyboardEnabled(context: Context): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.enabledInputMethodList.any { it.packageName == context.packageName }
}

@Composable
fun OnboardingFlow(onDone: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    var keyboardOk by remember { mutableStateOf(keyboardEnabled(context)) }
    var bubbleOk by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                keyboardOk = keyboardEnabled(context)
                bubbleOk = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val titles = listOf("GIFS ANYWHERE.", "ENABLE THE KEYBOARD.", "FLOATING BUBBLE.")
    val subs = listOf(
        "Create, convert and send looping GIFs from any app — keyboard, bubble and wallpapers included.",
        "Your GIF keyboard lives system-wide. Enable it once and send GIFs straight from any chat.",
        "One tap gives you Send, Save, Convert and Edit over whatever app you're in. Drag it anywhere."
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(InkBlack)
    ) {
        // Skip
        Text(
            "SKIP",
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = OffFaint,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .clickableNoRipple { onDone() }
        )

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(listOf(Yellow, Color(0xFFC9A300)))
                        )
                ) {
                    Text("G", fontFamily = Lilita, fontSize = 80.sp, color = InkBlack)
                }
                Text(
                    titles[page],
                    fontFamily = Lilita,
                    fontSize = 26.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    color = OffWhite,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    subs[page],
                    fontFamily = InterTight,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    color = OffDim,
                    modifier = Modifier.padding(top = 12.dp)
                )
                if (page == 1 && keyboardOk) {
                    Text(
                        "✓ KEYBOARD ENABLED",
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Yellow,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                if (page == 2 && bubbleOk) {
                    Text(
                        "✓ BUBBLE READY",
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Yellow,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                // progress dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 36.dp)
                ) {
                    repeat(3) { i ->
                        Box(
                            modifier = Modifier
                                .size(width = if (i == pagerState.currentPage) 22.dp else 7.dp, height = 7.dp)
                                .clip(if (i == pagerState.currentPage) RoundedCornerShape(4.dp) else CircleShape)
                                .background(if (i == pagerState.currentPage) Yellow else Charcoal2)
                                .clickableNoRipple { scope.launch { pagerState.animateScrollToPage(i) } }
                        )
                    }
                }
            }
        }

        // CTA
        val page = pagerState.currentPage
        val label = when (page) {
            0 -> "GET STARTED →"
            1 -> if (keyboardOk) "DONE →" else "ENABLE KEYBOARD →"
            else -> if (bubbleOk) "FINISH →" else "ENABLE BUBBLE →"
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Yellow)
                .border(2.dp, Yellow, RoundedCornerShape(16.dp))
                .clickableNoRipple {
                    when (page) {
                        0 -> scope.launch { pagerState.animateScrollToPage(1) }
                        1 -> {
                            if (keyboardOk) onDone()
                            else (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .showInputMethodPicker()
                        }
                        else -> {
                            if (bubbleOk) onDone()
                            else context.startActivity(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                            )
                        }
                    }
                }
                .padding(vertical = 16.dp)
        ) {
            Text(label, fontFamily = Lilita, fontSize = 15.sp, color = InkBlack)
        }
    }
}
