# GIF Anywhere

[![Build release APK](https://github.com/jacj90021-sys/gif/actions/workflows/build.yml/badge.svg)](https://github.com/jacj90021-sys/gif/actions/workflows/build.yml)

A native Android app (Kotlin + Jetpack Compose) implementing the full **GIF Anywhere** design system: yellow `#FFD400` on charcoal `#161615`/`#1F1F1E`, `Lilita One` display type, `Inter` body, `JetBrains Mono` readouts, with the exact border color `#2B2B29` used throughout.

## Releases (CI-built APK)

Every push to `main` builds a signed release APK and uploads it as a workflow artifact (Actions → Build release APK → GIF-Anywhere-release-apk). Push a version tag to also publish a GitHub Release:

```bash
git tag v1.0.0 && git push origin v1.0.0
```

Signing: release builds use `app/release.keystore` (alias/password `gifanywhere`, committed for CI reproducibility). Override with `KEYSTORE_PASSWORD` / `KEY_ALIAS` / `KEY_PASSWORD` env vars for production.

## Feature map

| Area | What's implemented |
|---|---|
| **Discover** | Search field (live filter), GIFs/Stickers/Memes segment, category chip row, Trending/Favorites/Recent segment, staggered masonry grid with LOOP badges, tap → action sheet (Send via system share, Save, Favorite, Edit → GIF Editor, Convert → Studio, Wallpaper → Library). All chips/segments are real single-select filters. |
| **Studio** | Source picker row (Gallery/Camera/Video/URL dialog/Library/Screen), 7-tile tools grid where every tile opens its **own distinct screen**, and the full Export panel: format chips (GIF/MP4/WebP/WebM/APNG), platform presets, Colors/FPS/Target-size sliders (fill + handle + value from one shared state), Batch export toggle, Export button with exporting → "✓ SAVED TO LIBRARY" transition. Exports appear in Library → Creations. |
| **Tool screens** | Video→GIF (trim range slider + edit chips), Boomerang (live preview area, record toggle, back-and-forth badge), Screen Rec (capture area/audio/duration settings + counting timer + auto-save), GIF Editor (full 8-tool grid: Trim/Crop/Speed/Reverse/Caption/Sticker/Filter/Watermark, each with its own contextual control panel), Meme Maker (live top/bottom text canvas, 2 inputs, 4 font chips that restyle the canvas), Sticker Maker (transparency checkerboard, cutout mask, auto-remove-bg + white-outline toggles that visibly change the preview), Merge/Combine (clip timeline with add-clip tile, selection, 3 layout modes that rearrange the preview). |
| **Library** | Favorites/Recent/Creations segments, folder collections grid with working "New Folder", wallpaper section (Home/Lock/Both toggle, 3-column grid with SET feedback), storage card with usage bar and working Clear Cache. **Unique empty state per segment** with its own copy. |
| **Settings** | Keyboard row with live ACTIVE / NOT ENABLED status pill (tap → system IME settings), Floating Bubble toggle (permission-gated, starts/stops the overlay service) with Bubble position/opacity rows shown only while enabled, Export defaults (format/quality/battery), General (theme/language/about dialog). Everything persists via SharedPreferences. |
| **Floating Bubble** | Real system overlay (`TYPE_APPLICATION_OVERLAY`): draggable, snaps to nearest screen edge on release (position persisted), tap → radial petal menu (Send / Save / Convert / Edit), tap a petal to run the action, tap outside to collapse, opacity applied from Settings. |
| **Keyboard** | Real `InputMethodService`: compact search, category chips, 4-column grid sized to the IME, bottom bar. Tapping a GIF uses `commitContent` when the focused editor accepts `image/gif`, otherwise inserts a GIF URL. |
| **Onboarding** | 3 swipeable screens (Intro / Enable keyboard / Enable bubble) with progress dots, skip, and system CTAs: IME picker and overlay-permission intent, with live ✓ ENABLED status feedback. Shown on first launch. |

## Content sources

GIF tiles use the reference's gradient placeholders (`Content.grads`). Wire the repository layer to Tenor/Giphy to swap in real looping media without touching any UI code.

## Build

Open the `gif` folder in Android Studio (Ladybug+) and let it sync (Gradle 8.7, AGP 8.5.2, Kotlin 2.0.20, compileSdk 35, minSdk 26), then Run.

`./gradlew` is not bundled in this repo (no wrapper JAR); Android Studio will provision Gradle from `gradle/wrapper/gradle-wrapper.properties`, or run `gradle wrapper` once you have Gradle installed.

### Try the system features on a device

1. Onboarding → "ENABLE KEYBOARD" → pick **GIF Anywhere Keyboard** in the system picker.
2. Settings → Floating Bubble → grant *Display over other apps* → the yellow bubble appears; drag it, tap it, use the petals.
3. Any app with a text field → open the GIF keyboard → tap a tile.

## Project layout

```
app/src/main/java/com/jacj90021/gifanywhere/
├── GifApp.kt                  # Application — loads persisted state
├── MainActivity.kt            # Nav host + onboarding gate + bubble deep links
├── OnboardingActivity.kt      # 3-screen pager flow
├── data/                      # Content mocks + persistent Store
├── ui/theme/                  # Exact palette + 3 font families
├── ui/components/             # Chips, segments, sliders, toggles, pills, buttons
├── ui/nav/                    # Bottom nav + NavHost
├── ui/screens/                # Discover, Studio(+Export panel), Library, Settings, ActionSheet
├── ui/screens/tools/          # The 7 distinct tool screens
├── bubble/BubbleService.kt    # Overlay bubble + petal menu
└── keyboard/GifKeyboardService.kt  # IME
```
