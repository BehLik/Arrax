package com.example.arrax.ui.screen.events.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import com.example.arrax.core.navigation.EventHome
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.usecase.business.members.observe.ObserveMembersUseCase
import com.example.arrax.domain.usecase.event.detail.ObserveEventDetailUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

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

@HiltViewModel
class EventHomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeEventDetail: ObserveEventDetailUseCase,
    observeMembers: ObserveMembersUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<EventHome>()

    private val _state = MutableStateFlow(EventHomeState.empty())
    val state: StateFlow<EventHomeState> = _state.asStateFlow()

    private val _events = Channel<EventHomeEvent>(Channel.BUFFERED)
    val events: Flow<EventHomeEvent> = _events.receiveAsFlow()
    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale("es", "MX"))
        .apply { timeZone = TimeZone.getTimeZone("UTC") }

    init {
        val membersFlow = observeMembers(route.businessId)
            .catch { emit(emptyList()) }

        observeEventDetail(route.businessId, route.eventId)
            .combine(membersFlow) { detail, members -> detail to members }
            .catch { Log.e("EventHomeViewModel", "Error observando evento", it) }
            .onEach { (detail, members) ->
                if (detail == null) return@onEach
                val event = detail.event
                val currentUid = FirebaseAuth.getInstance().currentUser?.uid

                _state.update {
                    it.copy(
                        eventName = event.name,
                        statusText = when (event.status) {
                            EventStatus.OPEN -> "Activo"
                            EventStatus.CLOSED -> "Cerrado"
                        },
                        dateFormatted = dateFormat.format(Date(event.date)),
                        animalCount = event.animalCount,
                        configuredCutsCount = detail.cuts.size,
                        createdBy = when {
                            event.createdBy == currentUid -> "Ti"
                            else -> members.firstOrNull { m -> m.uid == event.createdBy }?.name
                                ?: "Desconocido"
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBackClick() = send(EventHomeEvent.NavigateBack)
    fun onEditEventClick() = send(EventHomeEvent.EditEvent)
    fun onEditEventNameClick() = send(EventHomeEvent.EditEventName)
    fun onMakeOrdersClick() = send(EventHomeEvent.MakeOrders)
    fun onHandsFreeWeighingClick() = send(EventHomeEvent.HandsFreeWeighing)
    fun onMakeDeliveriesClick() = send(EventHomeEvent.MakeDeliveries)
    fun onSeeDetailClick() = send(EventHomeEvent.SeeDetail)
    fun onSeeAllOrdersClick() = send(EventHomeEvent.SeeAllOrders)
    fun onOrderClick(orderId: String) = send(EventHomeEvent.OpenOrder(orderId))

//    fun onBottomBarClick(route: String) {
//        if (route == _state.value.currentRoute) return
//        send(EventHomeEvent.NavigateToRoute(route))
//    }

    private fun send(event: EventHomeEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}