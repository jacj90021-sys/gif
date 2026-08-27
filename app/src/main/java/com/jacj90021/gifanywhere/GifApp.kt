package com.jacj90021.gifanywhere

import android.app.Application
import com.jacj90021.gifanywhere.data.Store

class GifApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Store.load(this)
    }
}
