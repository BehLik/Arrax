package com.example.arrax.ui.screen.business.customers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.business.BusinessAvatar
import com.example.arrax.ui.screen.business.BusinessListRow
import com.example.arrax.ui.screen.business.BusinessScreenSurface
import com.example.arrax.ui.screen.business.BusinessTabs
import com.example.arrax.ui.screen.business.BusinessTopBar

@Composable
fun BusinessCustomersScreen(
    modifier: Modifier = Modifier,
    onAddCustomerClick: () -> Unit = {},
    onCustomerClick: (customerId: String) -> Unit = {},
    viewModel: BusinessCustomersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessCustomersContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onTabSelected = viewModel::onTabSelected,
        onAddCustomerClick = onAddCustomerClick,
        onCustomerClick = onCustomerClick,
        modifier = modifier
    )
}

@Composable
internal fun BusinessCustomersContent(
    state: BusinessCustomersState,
    onQueryChange: (String) -> Unit,
    onTabSelected: (CustomersTab) -> Unit,
    onAddCustomerClick: () -> Unit,
    onCustomerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val glass = MaterialTheme.extendedColors
    val tabLabels = listOf(
        "Todos (${state.allCount})",
        "Frecuentes (${state.frequentCount})",
        "Nuevos (${state.newCount})"
    )

    BusinessScreenSurface(modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            BusinessTopBar(title = "Clientes") {
                IconButton(onClick = onAddCustomerClick) {
                    ArxIcon(
                        icon = ArxIcons.Add,
                        contentDescription = "Agregar cliente",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // TODO: migrar a ArxTextField cuando comparta su API.
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = ArxSpacing.md),
                singleLine = true,
                placeholder = { Text("Buscar cliente...") },
                leadingIcon = { ArxIcon(icon = ArxIcons.Search, size = 20.dp) },
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = glass.glass,
                    unfocusedContainerColor = glass.glass,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = glass.glassBorder
                )
            )

            BusinessTabs(
                labels = tabLabels,
                selectedIndex = CustomersTab.entries.indexOf(state.selectedTab),
                onSelect = { onTabSelected(CustomersTab.entries[it]) },
                modifier = Modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs)
            )

            if (state.customers.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ArxIcon(
                            icon = ArxIcons.SearchOff,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            size = 40.dp
                        )
                        Text(
                            text = "Sin resultados",
                            modifier = Modifier.padding(top = ArxSpacing.sm),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
                ) {
                    items(state.customers, key = { it.id }) { customer ->
                        BusinessListRow(
                            title = customer.name,
                            subtitle = customer.phone,
                            leading = { BusinessAvatar(initials = customer.initials) },
                            trailing = {
                                Text(
                                    text = if (customer.purchases == 1) "1 compra" else "${customer.purchases} compras",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            onClick = { onCustomerClick(customer.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(showBackground = true, widthDp = 360, heightDp = 780, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessCustomersPreview() {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        BusinessCustomersContent(
            state = BusinessCustomersState(
                allCount = 24, frequentCount = 8, newCount = 3,
                customers = listOf(
                    CustomerUi("1", "JP", "Juan Pérez", "+52 771 123 4567", 12),
                    CustomerUi("2", "ML", "María López", "+52 771 987 6543", 8),
                    CustomerUi("3", "CR", "Carlos Ramírez", "+52 771 444 5566", 5),
                    CustomerUi("4", "AT", "Ana Torres", "+52 772 222 4433", 4),
                    CustomerUi("5", "JM", "José Martínez", "+52 773 111 7799", 3)
                )
            ),
            onQueryChange = {}, onTabSelected = {}, onAddCustomerClick = {}, onCustomerClick = {}
        )
    }
}