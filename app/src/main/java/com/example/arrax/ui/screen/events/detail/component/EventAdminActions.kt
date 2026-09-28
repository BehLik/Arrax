package com.example.arrax.ui.screen.events.detail.component
import androidx.compose.runtime.Composable
//import com.example.arrax.core.designsystem.components.buttons.ArxButton
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton

@Composable
fun EventAdminActions(
    isClosing: Boolean,
    onCloseEventClick: () -> Unit
) {
    ArxPrimaryButton(
        text = "Cerrar evento",
        enabled = !isClosing,
        onClick = onCloseEventClick
    )
}
