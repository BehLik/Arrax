package com.example.arrax.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxPrimaryButton(
    text: String,
    onClick:()-> Unit,
    modifier: Modifier= Modifier,
    enabled: Boolean=true,
    leadingIcon:@Composable (()-> Unit)?=null,

    ){
    Button(
        onClick=onClick,
        enabled=enabled,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier.fillMaxWidth().height(56.dp)
    ) {
        if (leadingIcon != null){
            leadingIcon()
            Spacer(modifier= Modifier.width(8.dp))
        }
        Text(
            text=text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
@Composable
fun ArxSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier.fillMaxWidth().height(56.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ArxTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),

        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
@Composable
fun ArxFieldActionButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false, // Si es true, usa los colores primarios para resaltar
    onClick: () -> Unit
) {

    val containerColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isHighlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHighlighted) 4.dp else 0.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview(showBackground = false, name = "Familia de Botones Arrax")
@Composable
fun ArxButtonsPreview() {
    ArxTheme{
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ArxPrimaryButton(text = "Iniciar Sesión", onClick = {})

            ArxPrimaryButton(
                text = "Nuevo Lote",
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id= ArxIcons.Camera),
//                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null
                    )
                }
            )

            ArxSecondaryButton(text = "Editar Perfil", onClick = {})

            ArxTextButton(text = "¿Olvidaste tu contraseña?", onClick = {})
        }
    }
}
@Preview(showBackground =false, name = "Botones Rápidos de Campo")
@Composable
fun ArxFieldActionButtonsPreview() {
    ArxTheme {
        Row(modifier = Modifier.padding(16.dp)) {

            ArxFieldActionButton(
                iconRes = ArxIcons.NfcOutline,
                label = "Escanear",
                isHighlighted = true,
                modifier = Modifier.weight(1f),
                onClick = {}
            )

            Spacer(modifier = Modifier.width(16.dp))
            ArxFieldActionButton(
                iconRes = ArxIcons.Camera,
                label = "Tomar\nFoto",
                isHighlighted = false,
                modifier = Modifier.weight(1f),
                onClick = {}
            )
        }
    }
}