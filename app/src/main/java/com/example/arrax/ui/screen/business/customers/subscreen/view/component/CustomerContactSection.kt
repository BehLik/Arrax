package com.example.arrax.ui.screen.business.customers.subscreen.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.Customer

/** Teléfono y dirección. Cada bloque solo aparece si el dato existe (ambos son opcionales). */
@Composable
internal fun CustomerContactSection(
    customer: Customer,
    onCallClick: () -> Unit,
    onMapsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
    ) {
        if (customer.phone.isNotBlank()) {
            ArxSecondaryButton(
                text = "Llamar • ${customer.phone}",
                onClick = onCallClick,
                leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
            )
        }
        if (customer.address.isNotBlank()) {
            Text(
                text = customer.address,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (customer.reference.isNotBlank()) {
                Text(
                    text = "Referencia: ${customer.reference}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ArxSecondaryButton(
                text = "Abrir en Maps",
                onClick = onMapsClick,
                leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null) },
            )
        } else if (customer.reference.isNotBlank()) {
            Text(
                text = "Referencia: ${customer.reference}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}