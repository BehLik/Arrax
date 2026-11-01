package com.example.arrax.ui.root.notification

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import android.Manifest
import android.provider.Settings

@Stable
class NotificationPermissionController(private val context: Context) {
    var isGranted by mutableStateOf(systemEnabled())
        private set
    internal var launcher: ManagedActivityResultLauncher<String, Boolean>? = null
    private var pending: ((Boolean) -> Unit)? = null

    private fun systemEnabled() = NotificationManagerCompat.from(context).areNotificationsEnabled()

    /** Llamar en ON_RESUME: detecta cambios hechos desde los ajustes del teléfono. */
    fun refresh() {
        isGranted = systemEnabled()
        if (isGranted) pending?.let { it(true); pending = null }
    }

    fun request(onResult: (Boolean) -> Unit = {}) {
        if (isGranted) { onResult(true); return }
        pending = onResult
        if (Build.VERSION.SDK_INT >= 33) launcher?.launch(Manifest.permission.POST_NOTIFICATIONS)
        else openSystemSettings()
    }

    internal fun onPermissionResult(granted: Boolean) {
        refresh()
        if (granted) return
        val activity = context.findActivity()
        val blocked = activity != null && Build.VERSION.SDK_INT >= 33 &&
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)
        if (blocked) openSystemSettings() // bloqueado: el diálogo ya no sale, mandamos a ajustes
        else pending?.let { it(false); pending = null }
    }

    private fun openSystemSettings() {
        context.startActivity(
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

@Composable
fun rememberNotificationPermission(): NotificationPermissionController {
    val context = LocalContext.current
    val controller = remember { NotificationPermissionController(context) }
    controller.launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        controller.onPermissionResult(it)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { controller.refresh() }
    return controller
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}