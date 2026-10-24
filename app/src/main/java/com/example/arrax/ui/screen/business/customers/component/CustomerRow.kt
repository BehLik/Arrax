package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.customers.CustomerUi

@Composable
internal fun CustomerRow(
    customer: CustomerUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BusinessListRow(
        title = customer.name,
        modifier = modifier,
        subtitle = customer.phone,
        leading = { ArxAvatar(initials = customer.initials) },
        trailing = {
            Text(
                text = if (customer.purchases == 1) "1 compra" else "${customer.purchases} compras",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        onClick = { onClick(customer.id) }
    )
}