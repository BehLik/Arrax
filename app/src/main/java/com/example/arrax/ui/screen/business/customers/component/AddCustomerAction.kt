package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
internal fun AddCustomerAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        ArxIcon(
            icon = ArxIcons.Add,
            contentDescription = "Agregar cliente",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}