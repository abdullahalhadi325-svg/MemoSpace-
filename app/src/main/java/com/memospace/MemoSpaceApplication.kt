package com.memospace

import android.app.Application

/**
 * Base Application class for MemoSpace.
 * Prepares the offline-first application context and dependency architecture.
 */
class MemoSpaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialization for local databases and storage channels
    }
}
