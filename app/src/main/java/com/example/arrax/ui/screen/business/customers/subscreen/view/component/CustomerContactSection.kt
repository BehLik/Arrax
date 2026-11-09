package com.example.arrax.ui.screen.business.customers.subscreen.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.arrax.core.common.fullAddress
import com.example.arrax.core.common.fullPhone
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.icons.ArxBrandIcon
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.Customer

/** Teléfono y dirección. Cada bloque solo aparece si el dato existe (ambos son opcionales). */
@Composable
internal fun CustomerContactSection(
    customer: Customer,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onMapsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md),
    ) {
        if (customer.phone.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
            ) {
                ArxSecondaryButton(
                    text = "Llamar",
                    onClick = onCallClick,
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(painterResource(ArxIcons.Call), contentDescription = null) },
                )
                ArxSecondaryButton(
                    text = "WhatsApp",
                    onClick = onWhatsAppClick,
                    modifier = Modifier.weight(1f),
                    leadingIcon = { ArxBrandIcon(icon = ArxIcons.Whatsapp) },
                )
            }
        }
        if (customer.address.isNotBlank() || customer.reference.isNotBlank()) {
            CustomerCard {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md),
                ) {
                    ArxIcon(icon = ArxIcons.Location, tint = MaterialTheme.colorScheme.primary)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs),
                    ) {
                        if (customer.address.isNotBlank()) {
                            Text(
                                text = customer.fullAddress(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        if (customer.reference.isNotBlank()) {
                            Text(
                                text = "Referencia: ${customer.reference}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                if (customer.address.isNotBlank()) {
                    Spacer(Modifier.height(ArxSpacing.sm))
                    ArxSecondaryButton(
                        text = "Abrir en Maps",
                        onClick = onMapsClick,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { ArxBrandIcon(icon = ArxIcons.Maps) },
                    )
                }
            }
        }
    }
}