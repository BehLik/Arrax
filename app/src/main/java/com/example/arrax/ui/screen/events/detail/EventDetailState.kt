package com.example.arrax.ui.screen.events.detail

import com.example.arrax.domain.model.EventWithCuts

data class EventDetailState(
    val isLoading: Boolean = true,
    val eventWithCuts: EventWithCuts? = null,
    val isAdmin: Boolean = false,
    val isClosing: Boolean = false,
    val showCloseConfirmation: Boolean = false,
    val errorMessage: String? = null
)
