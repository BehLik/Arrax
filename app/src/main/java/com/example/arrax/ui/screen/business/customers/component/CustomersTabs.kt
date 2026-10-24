package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.inputs.ArxFilterChipGroup
import com.example.arrax.ui.screen.business.customers.CustomersTab

@Composable
internal fun CustomersTabs(
    selected: CustomersTab,
    allCount: Int,
    frequentCount: Int,
    newCount: Int,
    onSelected: (CustomersTab) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxFilterChipGroup(
        filters = CustomersTab.entries,
        selected = selected,
        onSelected = onSelected,
        label = { it.label() },
        modifier = modifier,
        // El grupo agrega " (n)" al texto cuando n > 0.
        badgeCounts = mapOf(
            CustomersTab.All to allCount,
            CustomersTab.Frequent to frequentCount,
            CustomersTab.New to newCount
        )
    )
}

internal fun CustomersTab.label(): String = when (this) {
    CustomersTab.All -> "Todos"
    CustomersTab.Frequent -> "Frecuentes"
    CustomersTab.New -> "Nuevos"
}