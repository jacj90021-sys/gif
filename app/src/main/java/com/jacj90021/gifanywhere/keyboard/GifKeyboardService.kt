package com.jacj90021.gifanywhere.keyboard

import android.content.ClipDescription
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.inputmethodservice.InputMethodService
import android.net.Uri
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputContentInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.inputmethod.EditorInfoCompat
import com.jacj90021.gifanywhere.R
import com.jacj90021.gifanywhere.data.Content

/**
 * System-wide GIF keyboard (real InputMethodService).
 * Compact search + category chips + 4-column grid sized to the IME height.
 * Tapping a GIF inserts it into whatever text field currently has focus:
 * commitContent when the editor accepts image/gif, otherwise a GIF URL.
 */
class GifKeyboardService : InputMethodService() {

    private lateinit var grid: GridLayout
    private val chipViews = mutableListOf<TextView>()
    private var selectedChip = 0
    private var query = ""
    private var editorSupportsGif = false

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun colorInt(c: androidx.compose.ui.graphics.Color): Int =
        android.graphics.Color.argb(
            (c.alpha * 255f).toInt(),
            (c.red * 255f).toInt(),
            (c.green * 255f).toInt(),
            (c.blue * 255f).toInt()
        )

    private fun cardBg(fillHex: String, strokeHex: String, radiusDp: Int, strokeDp: Int): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(radiusDp).toFloat()
            setColor(Color.parseColor(fillHex))
            setStroke(dp(strokeDp), Color.parseColor(strokeHex))
        }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        editorSupportsGif = editorInfo?.let {
            EditorInfoCompat.getContentMimeTypes(it).any { mime -> mime.equals("image/gif", true) }
        } ?: false
    }

    override fun onCreateInputView(): View {
        val mono: Typeface? = ResourcesCompat.getFont(this, R.font.jetbrains_mono_600)
        val inter: Typeface? = ResourcesCompat.getFont(this, R.font.inter_600)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#1F1F1E"))
            setPadding(dp(14), dp(12), dp(14), dp(16))
        }

        // ---- compact search ----
        val search = EditText(this).apply {
            hint = "Search GIFs, stickers, memes..."
            setTextColor(Color.parseColor("#FAFAF5"))
            setHintTextColor(Color.parseColor("#6D6B62"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            typeface = inter
            isSingleLine = true
            background = cardBg("#161615", "#2B2B29", 12, 2)
            setPadding(dp(12), dp(9), dp(12), dp(9))
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                query = s?.toString()?.trim() ?: ""
                rebuildGrid()
            }
        })
        root.addView(
            search,
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = dp(10)
            }
        )

        // ---- category chips ----
        val chipScroll = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
        val chipLayout = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        Content.categories.forEachIndexed { i, name ->
            val chip = TextView(this).apply {
                text = name
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.5f)
                typeface = inter
                setPadding(dp(11), dp(6), dp(11), dp(6))
                val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.marginEnd = dp(6)
                layoutParams = lp
                setOnClickListener { selectedChip = i; styleChips(); rebuildGrid() }
            }
            chipViews.add(chip)
            chipLayout.addView(chip)
        }
        styleChips()
        chipScroll.addView(chipLayout)
        root.addView(
            chipScroll,
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = dp(10)
            }
        )

        // ---- 4-column GIF grid ----
        grid = GridLayout(this).apply {
            columnCount = 4
            useDefaultMargins = false
        }
        root.addView(grid, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        rebuildGrid()

        // ---- bottom bar ----
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), dp(8), dp(4), 0)
        }
        val left = TextView(this).apply {
            text = "GIF ANYWHERE · IME"
            setTextColor(Color.parseColor("#6D6B62"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f)
            typeface = mono
        }
        val right = TextView(this).apply {
            text = "⌨ SWITCH"
            setTextColor(Color.parseColor("#6D6B62"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f)
            typeface = mono
            setOnClickListener { requestHideSelf(0) }
        }
        bar.addView(left, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        bar.addView(right)
        root.addView(bar)

        return root
    }

    private fun styleChips() {
        chipViews.forEachIndexed { i, chip ->
            val on = i == selectedChip
            chip.background = cardBg(if (on) "#FFD400" else "#161615", if (on) "#FFD400" else "#2B2B29", 16, 2)
            chip.setTextColor(Color.parseColor(if (on) "#0A0A0A" else "#B8B6AC"))
        }
    }

    private fun rebuildGrid() {
        grid.removeAllViews()
        val gap = dp(6)
        val rowWidth = resources.displayMetrics.widthPixels - dp(28)
        val cell = (rowWidth - 3 * gap) / 4

        val tiles = Content.kbTiles
            .mapIndexed { i, pair -> Triple(pair.first, pair.second, i % Content.categories.size) }
            .filter { (name, _, cat) ->
                (selectedChip == 0 || Content.categories[selectedChip] == Content.categories[cat]) &&
                    (query.isBlank() || name.contains(query, ignoreCase = true))
            }

        tiles.forEach { (name, gradIdx, _) ->
            val cellView = FrameLayout(this)
            cellView.background = GradientDrawable().apply {
                orientation = GradientDrawable.Orientation.TL_BR
                val g = Content.grads[gradIdx % Content.grads.size]
                colors = intArrayOf(colorInt(g.first), colorInt(g.second))
                cornerRadius = dp(9).toFloat()
            }
            cellView.setOnClickListener { insertGif(name) }

            val label = TextView(this).apply {
                text = name
                setTextColor(Color.parseColor("#FAFAF5"))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 8f)
                typeface = ResourcesCompat.getFont(this@GifKeyboardService, R.font.inter_700)
                setPadding(dp(4), 0, dp(4), dp(3))
            }
            cellView.addView(
                label,
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM or Gravity.START)
            )

            val lp = GridLayout.LayoutParams()
            lp.width = cell
            lp.height = cell
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            val glp = GridLayout.LayoutParams(lp)
            glp.setMargins(gap / 2, gap / 2, gap / 2, gap / 2)
            grid.addView(cellView, glp)
        }
    }

    private fun insertGif(name: String) {
        val ic: InputConnection = currentInputConnection ?: return
        if (editorSupportsGif) {
            runCatching {
                val info = InputContentInfo(
                    Uri.parse("content://com.jacj90021.gifanywhere/gifs/${name.lowercase().replace(' ', '_')}.gif"),
                    ClipDescription("GIF: $name", arrayOf("image/gif")),
                    null
                )
                if (ic.commitContent(info, 0, null)) return
            }
        }
        ic.commitText("https://gifanywhere.app/g/${name.lowercase().replace(' ', '_')}.gif ", 1)
    }
}
