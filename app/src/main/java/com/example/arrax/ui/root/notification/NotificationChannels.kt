package com.example.arrax.ui.root.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.ui.screen.lobby.profile.settings.component.subtitleRes
import com.example.arrax.ui.screen.lobby.profile.settings.component.titleRes

object NotificationChannels {
    fun id(channel: AlertChannel) = "alert_${channel.name.lowercase()}"

    fun register(context: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        val manager = context.getSystemService(NotificationManager::class.java)
        AlertChannel.entries.forEach { ch ->
            val importance = if (ch == AlertChannel.PROMOTIONS) NotificationManager.IMPORTANCE_LOW
            else NotificationManager.IMPORTANCE_DEFAULT
            manager.createNotificationChannel(
                NotificationChannel(id(ch), context.getString(ch.titleRes), importance)
                    .apply { description = context.getString(ch.subtitleRes) }
            )
        }
    }
}