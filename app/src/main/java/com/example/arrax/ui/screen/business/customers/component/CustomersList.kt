package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.CustomerUi

@Composable
internal fun CustomersList(
    customers: List<CustomerUi>,
    onCustomerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        items(customers, key = { it.id }) { customer ->
            CustomerRow(customer = customer, onClick = onCustomerClick)
        }
    }
}