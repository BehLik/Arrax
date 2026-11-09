package com.example.arrax.ui.screen.events.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/** Eventos de una sola vez (navegación / acciones) que la Screen traduce a callbacks. */
sealed interface EventHomeEvent {
    data object NavigateBack : EventHomeEvent
    data object EditEvent : EventHomeEvent
    data object EditEventName : EventHomeEvent
    data object MakeOrders : EventHomeEvent
    data object HandsFreeWeighing : EventHomeEvent
    data object MakeDeliveries : EventHomeEvent
    data object SeeDetail : EventHomeEvent
    data object SeeAllOrders : EventHomeEvent
    data class OpenOrder(val orderId: String) : EventHomeEvent
    data class NavigateToRoute(val route: String) : EventHomeEvent
}

class EventHomeViewModel : ViewModel() {

    // Arranca con datos de ejemplo (EventHomeState por defecto) hasta conectar el repositorio.
    private val _state = MutableStateFlow(EventHomeState())
    val state: StateFlow<EventHomeState> = _state.asStateFlow()

    private val _events = Channel<EventHomeEvent>(Channel.BUFFERED)
    val events: Flow<EventHomeEvent> = _events.receiveAsFlow()

    fun onBackClick() = send(EventHomeEvent.NavigateBack)
    fun onEditEventClick() = send(EventHomeEvent.EditEvent)
    fun onEditEventNameClick() = send(EventHomeEvent.EditEventName)
    fun onMakeOrdersClick() = send(EventHomeEvent.MakeOrders)
    fun onHandsFreeWeighingClick() = send(EventHomeEvent.HandsFreeWeighing)
    fun onMakeDeliveriesClick() = send(EventHomeEvent.MakeDeliveries)
    fun onSeeDetailClick() = send(EventHomeEvent.SeeDetail)
    fun onSeeAllOrdersClick() = send(EventHomeEvent.SeeAllOrders)
    fun onOrderClick(orderId: String) = send(EventHomeEvent.OpenOrder(orderId))

    fun onBottomBarClick(route: String) {
        if (route == _state.value.currentRoute) return
        send(EventHomeEvent.NavigateToRoute(route))
    }

    private fun send(event: EventHomeEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}