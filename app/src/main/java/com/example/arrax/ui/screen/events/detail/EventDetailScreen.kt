package com.example.arrax.ui.screen.events.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.feedback.ArxAlertDialog
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.ui.screen.events.detail.component.EventAdminActions
import com.example.arrax.ui.screen.events.detail.component.EventSummaryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val eventWithCuts = state.eventWithCuts

    Scaffold(
        topBar = {
            ArxTopBar(
                title = eventWithCuts?.event?.name?.ifBlank { null }
                    ?: stringResource(R.string.event_detail_title_fallback),
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> ArxLoading()
                eventWithCuts == null -> ArxEmptyState(
                    title = stringResource(R.string.event_not_found_title),
                    description = stringResource(R.string.event_not_found_description),
                    iconRes = ArxIcons.SearchOff
                )
                else -> {
                    Column(modifier = Modifier.fillMaxSize().padding(ArxSpacing.md)) {
                        EventSummaryCard(eventWithCuts = eventWithCuts)

                        if (state.isAdmin && eventWithCuts.event.status == EventStatus.OPEN) {
                            Spacer(modifier = Modifier.height(ArxSpacing.md))
                            EventAdminActions(
                                isClosing = state.isClosing,
                                onCloseEventClick = viewModel::onCloseEventClick
                            )
                        }
                    }
                }
            }

            if (state.showCloseConfirmation) {
                ArxAlertDialog(
                    title = stringResource(R.string.event_close_title),
                    message = stringResource(R.string.event_close_message),
                    confirmText = stringResource(R.string.event_close_confirm),
                    onConfirm = viewModel::onConfirmCloseEvent,
                    onDismiss = viewModel::onDismissCloseConfirmation
                )
            }
        }
    }
}
