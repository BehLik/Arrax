package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.screen.lobby.home.NegocioResumen

@Composable
fun NegocioListItem(
    negocio: NegocioResumen,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rol = if (negocio.esPropietario) "Propietario" else "Miembro"
    val eventos = if (negocio.totalEventos == 1) "1 evento" else "${negocio.totalEventos} eventos"

    ArxCard(variant = ArxCardVariant.Standard, onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ArxAvatar(initials = arxInitials(negocio.nombre), size = 44.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = negocio.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$rol · $eventos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                painter = painterResource(ArxIcons.Forward),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}