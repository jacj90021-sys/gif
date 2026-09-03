package com.jacj90021.gifanywhere.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.jacj90021.gifanywhere.R

object AppIcons {
    // Tab icons (resource IDs)
    val Discover = R.drawable.ic_discover
    val Studio = R.drawable.ic_studio
    val Library = R.drawable.ic_library
    val Settings = R.drawable.ic_settings

    // Action sheet
    val Send = R.drawable.ic_send
    val Save = R.drawable.ic_bookmark
    val Favorite = R.drawable.ic_heart
    val Edit = R.drawable.ic_edit
    val Convert = R.drawable.ic_loop
    val Wallpaper = R.drawable.ic_image

    // Source row
    val Gallery = R.drawable.ic_photo_library
    val Camera = R.drawable.ic_camera
    val Video = R.drawable.ic_video
    val Url = R.drawable.ic_link

    // Tools
    val VideoToGif = R.drawable.ic_play
    val Boomerang = R.drawable.ic_loop
    val ScreenRec = R.drawable.ic_monitor
    val GifEditor = R.drawable.ic_edit
    val MemeMaker = R.drawable.ic_meme

    @Composable
    fun painter(id: Int) = painterResource(id)
}
