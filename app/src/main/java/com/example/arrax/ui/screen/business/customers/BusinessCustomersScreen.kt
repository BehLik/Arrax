package com.example.arrax.ui.screen.business.customers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.backgrounds.ArxScreenSurface
import com.example.arrax.core.designsystem.components.inputs.ArxSearchField
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.customers.component.AddCustomerAction
import com.example.arrax.ui.screen.business.customers.component.CustomersEmptyState
import com.example.arrax.ui.screen.business.customers.component.CustomersList
import com.example.arrax.ui.screen.business.customers.component.CustomersTabs

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessCustomersContent(
    state: BusinessCustomersState,
    onQueryChange: (String) -> Unit,
    onTabSelected: (CustomersTab) -> Unit,
    onAddCustomerClick: () -> Unit,
    onCustomerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

        ArxBackground(decoration = ArxDecoration.None) {
            Column(modifier = Modifier.fillMaxSize()) {
                ArxTopBar(
                    title = "Clientes",
                    centeredTitle = false,
                    actions = { AddCustomerAction(onClick = onAddCustomerClick) }
                )

                ArxSearchField(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    placeholder = "Buscar cliente...",
                    modifier = Modifier.padding(horizontal = ArxSpacing.md)
                )

                CustomersTabs(
                    selected = state.selectedTab,
                    allCount = state.allCount,
                    frequentCount = state.frequentCount,
                    newCount = state.newCount,
                    onSelected = onTabSelected
                )

                if (state.customers.isEmpty()) {
                    CustomersEmptyState(
                        isSearching = state.query.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        onAddClick ={}
                    )
                } else {
                    CustomersList(
                        customers = state.customers,
                        onCustomerClick = onCustomerClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(
    showBackground = true, widthDp = 360, heightDp = 780,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
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