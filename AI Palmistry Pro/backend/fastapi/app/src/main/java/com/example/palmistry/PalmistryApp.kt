package com.example.palmistry

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PalmistryApp : Application() {
    // Hilt will set up dependency injection for the whole app.
}
