package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.feedback.ArxBottomSheet
import com.example.arrax.core.designsystem.components.feedback.ArxBottomSheetSize
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

/** Paso intermedio antes de crear un evento: explica qué es y qué se puede hacer con él. */
@Composable
internal fun EventIntroSheet(
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    ArxBottomSheet(
        onDismissRequest = onDismiss,
        size = ArxBottomSheetSize.WrapContent,
        title = "¿Qué es un evento?",
        subtitle = "Tu cuaderno de trabajo para cada venta.",
        icon = ArxIcons.Event,
        showCloseButton = true,
        footer = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ArxPrimaryButton(text = "Continuar", onClick = onContinue)
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)) {
            BusinessAndEventAnalogy()

            IntroPoint(
                icon = ArxIcons.Order,
                title = "Pedidos de tus clientes",
                description = "Registra pedidos con los clientes de tu negocio y los cortes que tú definiste."
            )
            IntroPoint(
                icon = ArxIcons.Worker,
                title = "En equipo",
                description = "Tus coworkers registran pedidos y pagos en el mismo evento."
            )
            IntroPoint(
                icon = ArxIcons.Payment,
                title = "Pagos al día",
                description = "Lleva el control de lo que cada cliente ya pagó y lo que falta."
            )

            WeighingAssistantHighlight()
        }
    }
}

@Composable
private fun BusinessAndEventAnalogy() {
    Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnalogyItem(icon = ArxIcons.Storage, label = "Negocio", caption = "La biblioteca")
            ArxIcon(
                icon = ArxIcons.Forward,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                size = 20.dp
            )
            AnalogyItem(icon = ArxIcons.Event, label = "Evento", caption = "El libro")
        }
        Text(
            text = "Cada evento guarda todo lo que pasa en una venta, de principio a fin.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AnalogyItem(icon: Int, label: String, caption: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ArxIconBadge(icon = icon, style = ArxIconStyle.Tonal, shape = ArxIconShape.Circle)
        Text(label, style = MaterialTheme.typography.titleSmall)
        Text(
            caption,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IntroPoint(icon: Int, title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
    ) {
        ArxIconBadge(icon = icon, style = ArxIconStyle.Soft)
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Es la función estrella: se destaca con primaryContainer y badge sólido. */
@Composable
private fun WeighingAssistantHighlight() {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = colors.primaryContainer,
        contentColor = colors.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            ArxIconBadge(icon = ArxIcons.Scale, style = ArxIconStyle.Solid, shape = ArxIconShape.Circle)
            Column(modifier = Modifier.weight(1f)) {
                Text("Asistente de pesaje manos libres", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Un asistente de voz te acompaña al pesar para que trabajes sin soltar lo que tienes en las manos.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}