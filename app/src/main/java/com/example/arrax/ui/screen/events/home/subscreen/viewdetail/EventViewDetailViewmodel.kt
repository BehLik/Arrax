package com.example.arrax.ui.screen.events.home.subscreen.viewdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.arrax.core.navigation.EventDetail
import com.example.arrax.domain.model.Access
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.domain.model.Permission
import com.example.arrax.domain.usecase.access.ObserveAccessUseCase
import com.example.arrax.domain.usecase.business.members.observe.ObserveMembersUseCase
import com.example.arrax.domain.usecase.catalog.GetCatalogCutUseCase
import com.example.arrax.domain.usecase.event.detail.ObserveEventDetailUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class EventViewDetailViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeEventDetail: ObserveEventDetailUseCase,
    observeMembers: ObserveMembersUseCase,
    observeCatalog: GetCatalogCutUseCase,
//    observeCatalog: ObserveCatalogUseCase,
    observeAccess: ObserveAccessUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<EventDetail>()

    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale("es", "MX"))
        .apply { timeZone = TimeZone.getTimeZone("UTC") }

    val state: StateFlow<EventViewDetailState> = combine(
        observeEventDetail(route.businessId, route.eventId),
        observeMembers(route.businessId).catch { emit(emptyList()) },
        observeCatalog(route.businessId).catch { emit(emptyList()) },
        observeAccess(route.businessId)
    ) { detail, members, catalog, access ->
        if (detail == null) return@combine EventViewDetailState(isLoading = true)
        val event = detail.event
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        EventViewDetailState(
            isLoading = false,
            eventName = event.name,
            statusText = if (event.status == EventStatus.OPEN) "Activo" else "Cerrado",
            isOpen = event.status == EventStatus.OPEN,
            dateFormatted = dateFormat.format(Date(event.date)),
            animalCount = event.animalCount,
            createdBy = when {
                event.createdBy == currentUid -> "Ti"
                else -> members.firstOrNull { it.uid == event.createdBy }?.name ?: "Desconocido"
            },
            cuts = detail.cuts.map { c ->
                EventCutDetailUi(c.cutId, c.name, c.pricePerKgCents, catalog.firstOrNull { it.id == c.cutId })
            },
            canEdit = access is Access.Granted && access.can(Permission.EVENT_EDIT)
        )
    }
        .catch { Log.e("EventViewDetailVM", "Error observando evento", it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EventViewDetailState())
}