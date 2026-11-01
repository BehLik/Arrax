package com.example.arrax

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.arrax.ui.root.notification.NotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArraxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        NotificationChannels.register(this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_GENERAL,
            "General",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_GENERAL = "arrax_general"
    }
}