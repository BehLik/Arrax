package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.domain.model.CutCatalog

@Composable
fun CutImage(cut: CutCatalog, modifier: Modifier = Modifier) {
    val url = cut.imageUrl
    val res = cut.imageRes
    val fallback = res?.let { painterResource(it) }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        when {
            !url.isNullOrBlank() -> AsyncImage(
                model = url,
                contentDescription = cut.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = fallback,
                error = fallback
            )
            fallback != null -> Image(
                painter = fallback,
                contentDescription = cut.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            else -> ArxIcon(ArxIcons.Box, tint = MaterialTheme.colorScheme.onSurfaceVariant, size = 32.dp)
        }
    }
}