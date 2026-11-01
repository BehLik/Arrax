package com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.arrax.core.common.toLabel
import com.example.arrax.core.designsystem.components.buttons.ArxPrimaryButton
import com.example.arrax.core.designsystem.components.inputs.ArxSelectableCard
import com.example.arrax.core.designsystem.components.inputs.ArxStepper
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.InviteType
import com.example.arrax.domain.model.Role
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.InvitationCreateState
import com.example.arrax.ui.screen.business.more.subscreen.members.subscreen.invitation.toLabel


@Composable
internal fun InvitationForm(
    state: InvitationCreateState,
    onTypeSelected: (InviteType) -> Unit,
    onRoleSelected: (Role) -> Unit,
    onPeopleChange: (Int) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ArxSpacing.md),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg)
    ) {
        Text(
            "Selecciona el tipo de acceso y la cantidad de personas que podrán usar la invitación.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            Text("Tipo de acceso", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                TypeOption(InviteType.CODE_QR, "Más práctico y flexible.", ArxIcons.ScanQr, state.type, onTypeSelected, Modifier.weight(1f))
                TypeOption(InviteType.CODE, "Solo el código. Útil por chat.", ArxIcons.Link, state.type, onTypeSelected, Modifier.weight(1f))
                TypeOption(InviteType.QR, "Solo el QR. Rápido y directo.", ArxIcons.ScanQr, state.type, onTypeSelected, Modifier.weight(1f))
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            Text("Rol de usuario", style = MaterialTheme.typography.titleMedium)
            RoleOption(Role.OPERATOR, "Registra y opera el día a día.", state.role, onRoleSelected)
            // Descomenta cuando las reglas de "unirse" acepten ADMIN:
            // RoleOption(Role.ADMIN, "Gestiona eventos, clientes y usuarios.", state.role, onRoleSelected)
        }

        Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
            Text("Cantidad de personas", style = MaterialTheme.typography.titleMedium)
            ArxStepper(value = state.people, onValueChange = onPeopleChange, range = 1..10)
            Text(
                "Máximo 10 personas por invitación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        ArxPrimaryButton(text = "Siguiente", onClick = onNext, loading = state.isCreating)
        Spacer(Modifier.height(ArxSpacing.md))
    }
}

@Composable
private fun TypeOption(
    type: InviteType,
    description: String,
    @DrawableRes icon: Int,
    selected: InviteType,
    onSelect: (InviteType) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxSelectableCard(selected = type == selected, onClick = { onSelect(type) }, modifier = modifier) {
        ArxIcon(icon = icon, tint = MaterialTheme.colorScheme.primary)
        Text(type.toLabel(), style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center)
        Text(
            description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RoleOption(
    role: Role,
    description: String,
    selected: Role,
    onSelect: (Role) -> Unit
) {
    ArxSelectableCard(selected = role == selected, onClick = { onSelect(role) }, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)) {
            ArxIcon(icon = ArxIcons.User, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f)) {
                Text(role.toLabel(), style = MaterialTheme.typography.titleSmall)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RadioButton(selected = role == selected, onClick = null)
        }
    }
}