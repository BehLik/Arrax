package com.example.arrax.ui.screen.business.customers.subscreen.view

import com.example.arrax.domain.model.Customer

data class CustomerViewState(
    val isLoading: Boolean = true,
    val customer: Customer? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false,
    val message: String? = null,
    val deleted: Boolean = false,
)

data class CustomerViewActions(
    val onBack: () -> Unit = {},
    val onEditClick: () -> Unit = {},
    val onCallClick: () -> Unit = {},
    val onMapsClick: () -> Unit = {},
    val onWhatsAppClick: () -> Unit = {},
    val onDeleteRequest: () -> Unit = {},
    val onDeleteConfirm: () -> Unit = {},
    val onDeleteDismiss: () -> Unit = {},
    val onMessageShown: () -> Unit = {},
)