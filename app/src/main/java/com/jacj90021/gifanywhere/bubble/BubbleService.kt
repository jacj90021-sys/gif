package com.jacj90021.gifanywhere.bubble

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.IBinder
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.jacj90021.gifanywhere.MainActivity
import com.jacj90021.gifanywhere.R
import kotlin.math.abs

/**
 * Floating bubble: a system overlay window (TYPE_APPLICATION_OVERLAY).
 * Drag to move — snaps to the nearest screen edge on release.
 * Tap to expand the radial petal menu (Send / Save / Convert / Edit).
 * Tap any petal to perform that action; tap outside to collapse.
 */
class BubbleService : Service() {

    private lateinit var wm: WindowManager
    private var bubbleView: View? = null
    private var bubbleParams: WindowManager.LayoutParams? = null
    private var expandedRoot: FrameLayout? = null
    private var expanded = false
    private var lastX = 0
    private var lastY = 0

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private val prefs by lazy { getSharedPreferences("gif_anywhere", MODE_PRIVATE) }

    private fun bubbleOpacityFraction(): Float = prefs.getInt("bubbleOpacity", 90) / 100f
    private fun bubbleSide(): String = prefs.getString("bubbleSide", "Right") ?: "Right"
    private fun bubbleY(): Float = prefs.getFloat("bubbleY", 0.8f)

    private fun persistBubble(side: String, y: Float) {
        prefs.edit()
            .putString("bubbleSide", side)
            .putFloat("bubbleY", y)
            .apply()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        showBubble()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bubbleView?.alpha = bubbleOpacityFraction()
        return START_STICKY
    }

    override fun onDestroy() {
        if (expanded) expandedRoot?.let { runCatching { wm.removeView(it) } }
        bubbleView?.let { runCatching { wm.removeView(it) } }
        expandedRoot = null
        bubbleView = null
        super.onDestroy()
    }

    /* ---------- collapsed bubble ---------- */

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun showBubble() {
        lastX = if (bubbleSide() == "Left") dp(12)
        else resources.displayMetrics.widthPixels - dp(58) - dp(12)
        lastY = (resources.displayMetrics.heightPixels * bubbleY()).toInt()
            .coerceIn(dp(40), resources.displayMetrics.heightPixels - dp(120))

        val view = BubbleView(this, ring = true)
        val params = WindowManager.LayoutParams(
            dp(58), dp(58),
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = lastX
            y = lastY
        }

        var downRawX = 0f
        var downRawY = 0f
        var startPX = 0
        var startPY = 0
        var moved = false

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downRawX = event.rawX
                    downRawY = event.rawY
                    startPX = params.x
                    startPY = params.y
                    moved = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - downRawX
                    val dy = event.rawY - downRawY
                    if (abs(dx) > dp(6) || abs(dy) > dp(6)) moved = true
                    if (moved) {
                        params.x = startPX + dx.toInt()
                        params.y = (startPY + dy.toInt())
                            .coerceIn(dp(20), resources.displayMetrics.heightPixels - dp(80))
                        wm.updateViewLayout(v, params)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!moved) toggleExpand()
                    else snapToEdge(v, params)
                    true
                }
                else -> false
            }
        }

        // If overlay permission was revoked mid-session, addView throws
        // SecurityException — degrade gracefully instead of crashing.
        runCatching { wm.addView(view, params) }
            .onSuccess {
                bubbleView = view
                bubbleParams = params
                view.alpha = bubbleOpacityFraction()
            }
            .onFailure { stopSelf() }
    }

    @SuppressLint("RtlHardcoded")
    private fun snapToEdge(v: View, params: WindowManager.LayoutParams) {
        val screenW = resources.displayMetrics.widthPixels
        val target = if (params.x + dp(29) < screenW / 2) dp(12)
        else screenW - dp(58) - dp(12)
        params.x = target
        runCatching { wm.updateViewLayout(v, params) }
        lastX = target
        lastY = params.y
        val side = if (target < screenW / 2) "Left" else "Right"
        val y = (params.y.toFloat() / resources.displayMetrics.heightPixels).coerceIn(0.05f, 0.95f)
        persistBubble(side, y)
    }

    /* ---------- expanded petal menu ---------- */

    private fun toggleExpand() {
        if (expanded) collapse() else expand()
    }

    @SuppressLint("RtlHardcoded")
    private fun expand() {
        val bubble = bubbleView ?: return
        val params = bubbleParams ?: return
        if (expanded) return
        expanded = true
        lastX = params.x
        lastY = params.y
        runCatching { wm.removeView(bubble) }

        val root = FrameLayout(this)
        val dim = View(this)
        dim.setBackgroundColor(0x99000000.toInt())
        dim.setOnClickListener { collapse() }
        root.addView(
            dim,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val menu = FrameLayout(this)
        val menuLp = FrameLayout.LayoutParams(dp(58), dp(58))
        menuLp.leftMargin = lastX
        menuLp.topMargin = lastY

        val center = BubbleView(this, ring = false)
        center.alpha = bubbleOpacityFraction()
        center.setOnClickListener { collapse() }
        menu.addView(
            center,
            FrameLayout.LayoutParams(dp(58), dp(58), Gravity.CENTER)
        )

        val petals = listOf("➤", "★", "⇄", "✎")
        val offsets = listOf(-66 to -66, 66 to -66, -66 to 66, 66 to 66)
        petals.forEachIndexed { i, glyph ->
            val petal = FrameLayout(this)
            petal.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#FFFFFF"))
                setStroke(dp(2), Color.parseColor("#0A0A0A"))
            }
            petal.elevation = dp(6).toFloat()
            val tv = TextView(this).apply {
                text = glyph
                setTextColor(Color.parseColor("#0A0A0A"))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                gravity = Gravity.CENTER
            }
            petal.addView(tv, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            petal.setOnClickListener {
                petalAction(i)
                collapse()
            }
            val lp = FrameLayout.LayoutParams(dp(46), dp(46), Gravity.CENTER)
            petal.layoutParams = lp
            petal.translationX = dp(offsets[i].first).toFloat()
            petal.translationY = dp(offsets[i].second).toFloat()
            petal.scaleX = 0f
            petal.scaleY = 0f
            petal.animate().scaleX(1f).scaleY(1f).setDuration(160).setStartDelay(i * 30L).start()
            menu.addView(petal)
        }

        root.addView(menu, menuLp)
        expandedRoot = root
        wm.addView(
            root,
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 0
                y = 0
            }
        )
    }

    @SuppressLint("RtlHardcoded")
    private fun collapse() {
        val root = expandedRoot ?: return
        runCatching { wm.removeView(root) }
        expandedRoot = null
        expanded = false
        showBubble()
    }

    private fun petalAction(i: Int) {
        when (i) {
            0 -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Check out this GIF! 🎞")
                }
                runCatching {
                    startActivity(
                        Intent.createChooser(send, "Send GIF")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
            1 -> Toast.makeText(this, "Saved to Library", Toast.LENGTH_SHORT).show()
            2 -> openApp("studio")
            3 -> openApp("tool/editor")
        }
    }

    private fun openApp(route: String) {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("route", route)
        )
    }

    /* ---------- bubble drawing ---------- */

    private inner class BubbleView(context: android.content.Context, val ring: Boolean) :
        View(context) {

        private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFD400.toInt()
            style = Paint.Style.FILL
        }
        private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0x66FFD400
            style = Paint.Style.STROKE
            strokeWidth = dp(2).toFloat()
        }
        private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF0A0A0A.toInt()
            typeface = ResourcesCompat.getFont(context, R.font.lilita_one) ?: Typeface.DEFAULT_BOLD
            textSize = dp(20).toFloat()
            textAlign = Paint.Align.CENTER
        }

        override fun onDraw(canvas: android.graphics.Canvas) {
            super.onDraw(canvas)
            val cx = width / 2f
            val cy = height / 2f
            val r = width / 2f - dp(2)
            canvas.drawCircle(cx, cy, r, fill)
            if (ring) canvas.drawCircle(cx, cy, r + dp(4), ringPaint)
            val fm = text.fontMetrics
            canvas.drawText("G", cx, cy - (fm.ascent + fm.descent) / 2f, text)
        }
    }
}
