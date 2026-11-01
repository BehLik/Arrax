package com.example.arrax.ui.screen.lobby.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.agoLabel
import com.example.arrax.core.common.minuteTicker
import com.example.arrax.core.common.toLabel
import com.example.arrax.domain.model.NotificationType
import com.example.arrax.domain.model.TenantNotification
import com.example.arrax.domain.usecase.notifications.MarkNotificationReadUseCase
import com.example.arrax.domain.usecase.notifications.ObserveNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyNotificationViewModel @Inject constructor(
    observeNotifications: ObserveNotificationsUseCase,
    private val markNotificationRead: MarkNotificationReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyNotificationState())
    val uiState: StateFlow<LobbyNotificationState> = _uiState.asStateFlow()

    init {
        combine(observeNotifications(), minuteTicker()) { list, now ->
            list.map { it.toUi(now) }
        }
            .onEach { ui -> _uiState.update { it.copy(isLoading = false, notifications = ui, error = null) } }
            .catch { _uiState.update { it.copy(isLoading = false, error = "No se pudieron cargar las notificaciones.") } }
            .launchIn(viewModelScope)
    }

    fun onNotificationClick(id: String) {
        val (tenantId, notifId) = id.split("/", limit = 2).takeIf { it.size == 2 } ?: return
        if (_uiState.value.notifications.firstOrNull { it.id == id }?.isRead == true) return
        viewModelScope.launch { markNotificationRead(tenantId, notifId) }
    }

    // El id de la UI combina negocio y documento, porque la lista mezcla varios negocios.
    private fun TenantNotification.toUi(now: Long) = NotificationUi(
        "$tenantId/$id",
        when (type) {
            NotificationType.MEMBER_JOINED -> "Nuevo integrante en $tenantName"
            NotificationType.UNKNOWN -> "Notificación"
        },
        when (type) {
            NotificationType.MEMBER_JOINED -> "$actorName se unió como ${role.toLabel()}."
            NotificationType.UNKNOWN -> ""
        },
        agoLabel(createdAt, now),
        isRead
    )
}