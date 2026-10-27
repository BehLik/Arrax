package com.example.arrax.ui.screen.business.members.component

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import java.io.File

object InviteSharing {
    fun shareText(context: Context, message: String) {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(send, "Compartir invitación"))
    }

    fun shareQr(context: Context, qr: Bitmap, message: String) {
        val dir = File(context.cacheDir, "invites").apply { mkdirs() }
        val file = File(dir, "arrax_invitacion.png")
        file.outputStream().use { qr.compress(Bitmap.CompressFormat.PNG, 100, it) }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, "Compartir QR"))
    }
}