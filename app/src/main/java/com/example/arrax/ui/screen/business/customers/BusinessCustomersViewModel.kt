package com.example.arrax.ui.screen.business.customers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.navigation.BusinessCustomers
import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.usecase.customer.Observe.ObserveCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class BusinessCustomersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeCustomers: ObserveCustomersUseCase,
) : ViewModel() {

    private val businessId = savedStateHandle.toRoute<BusinessCustomers>().businessId
    private val query = MutableStateFlow("")
    private val tab = MutableStateFlow(CustomersTab.All)

    val uiState: StateFlow<BusinessCustomersState> =
        combine(observeCustomers(businessId), query, tab) { all, q, t ->
            val now = System.currentTimeMillis()
            fun isNew(c: Customer) = now - c.createdAt < NEW_WINDOW_MS
            val filtered = all
                .filter {
                    when (t) {
                        CustomersTab.All -> true
                        CustomersTab.Frequent -> false // TODO(F4): según pedidos
                        CustomersTab.New -> isNew(it)
                    }
                }
                .filter {
                    q.isBlank() ||
                            it.name.contains(q.trim(), ignoreCase = true) ||
                            it.phone.contains(q.trim())
                }
            BusinessCustomersState(
                query = q,
                selectedTab = t,
                allCount = all.size,
                frequentCount = 0,
                newCount = all.count(::isNew),
                customers = filtered.map {
                    CustomerUi(
                        id = it.id,
                        initials = arxInitials(it.name),
                        name = it.name,
                        phone = it.phone,
                        purchases = 0, // TODO(F4)
                    )
                },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BusinessCustomersState())

    fun onQueryChange(q: String) { query.value = q }
    fun onTabSelected(t: CustomersTab) { tab.value = t }

    private companion object {
        const val NEW_WINDOW_MS = 7L * 24 * 60 * 60 * 1000
    }
}