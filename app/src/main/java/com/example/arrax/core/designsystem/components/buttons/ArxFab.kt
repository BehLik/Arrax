package com.example.arrax.core.designsystem.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.icons.ArxIcons
//import com.example.arrax.core.desigsystem.theme.AppTheme
import com.example.arrax.core.designsystem.theme.ArxTheme

@Composable
fun ArxFab (
    icon: Painter,
    onClick:()-> Unit,
    contentDescription: String,
    modifier: Modifier= Modifier
){
    FloatingActionButton(
        onClick=onClick,
        modifier=modifier,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation =12.dp
        )
    ) {
        Icon(painter = icon,
            contentDescription = contentDescription)
    }
}
@Composable
fun ArxExtendedFAB(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier= Modifier,
    expanded: Boolean=true
){
    ExtendedFloatingActionButton(
        onClick=onClick,
        modifier=modifier,
        shape = MaterialTheme.shapes.large,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary,
        expanded = expanded,
        icon={
            Icon(painter = icon,contentDescription=null)
        },
        text = {
            Text(
                text=text.uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        },
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp
        )
    )
}
@Preview(showBackground = false, name = "Familia de Botones Flotantes (FAB)")
@Composable
fun AviFABsPreview() {
    ArxTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Ejemplo de uso para el Operador (Escáner)
            ArxFab(
                icon = painterResource(id = ArxIcons.NfcOutline),
                contentDescription = "Escanear Gallina",
                onClick = {}
            )

            // Ejemplo de uso para el Productor (Nuevo Registro)
            ArxExtendedFAB(
                text = "Nuevo Lote",
                icon = painterResource(id = ArxIcons.Add),
                onClick = {}
            )

            // Ejemplo de cómo se ve el Extendido cuando el usuario hace scroll
            ArxExtendedFAB(
                text = "Nuevo Lote",
                icon = painterResource(id = ArxIcons.Add),
                onClick = {},
                expanded = false
            )
        }
    }
}