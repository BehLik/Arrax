package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors

/**
 * Check de éxito (comando reconocido / peso guardado). Aparece con fade + escala en 250 ms,
 * sin rebotes. [haptic] = true da una vibración corta de confirmación.
 */
@Composable
fun ArxSuccessCheck(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    haptic: Boolean = false,
    contentDescription: String = "Confirmado"
) {
    var visible by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(250),
        label = "success_check"
    )
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        visible = true
        if (haptic) hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                alpha = progress
                scaleX = 0.6f + 0.4f * progress
                scaleY = 0.6f + 0.4f * progress
            }
            .clip(CircleShape)
            .background(MaterialTheme.extendedColors.glass)
            .border(2.dp, MaterialTheme.extendedColors.success.color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = ArxIcons.Check),
            contentDescription = contentDescription,
            tint = MaterialTheme.extendedColors.success.color,
            modifier = Modifier.size(size / 2)
        )
    }
}

@Composable
private fun ArxSuccessCheckPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ArxSuccessCheck(modifier = Modifier.padding(ArxSpacing.md))
        }
    }
}

@Preview(name = "Check de éxito – Light", showBackground = false)
@Composable
fun ArxSuccessCheckPreviewLight() = ArxSuccessCheckPreviewContent()

@Preview(name = "Check de éxito – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxSuccessCheckPreviewDark() = ArxSuccessCheckPreviewContent()