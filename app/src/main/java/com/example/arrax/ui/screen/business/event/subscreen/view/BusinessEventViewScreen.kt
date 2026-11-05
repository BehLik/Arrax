package com.example.arrax.ui.screen.business.event.subscreen.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.cards.ArxCardVariant
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.feedback.ArxChipStatus
import com.example.arrax.core.designsystem.components.feedback.ArxStatusChip
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.Event
import com.example.arrax.domain.model.EventStatus
import com.example.arrax.ui.screen.business.event.component.animalsLabel
import com.example.arrax.ui.screen.business.event.component.formatDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessEventViewScreen(
    onBack: () -> Unit,
    onEventClick: (eventId: String) -> Unit,
    viewModel: BusinessEventViewViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { snackbar.showSnackbar(it); viewModel.onMessageShown() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            ArxTopBar(
                title = "Eventos",
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                EventFilter.entries.forEach { f ->
                    FilterChip(
                        selected = state.filter == f,
                        onClick = { viewModel.onFilterChange(f) },
                        label = { Text("${f.label} (${state.countOf(f)})") }
                    )
                }
            }

            when {
                state.isLoading -> ArxLoading()
                state.visibleEvents.isEmpty() -> ArxEmptyState(
                    title = "Sin eventos",
                    description = if (state.filter == EventFilter.ALL) "Crea tu primer evento desde el botón + del inicio."
                    else "No hay eventos en esta categoría.",
                    iconRes = ArxIcons.Event
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
                ) {
                    items(state.visibleEvents, key = { it.id }) { event ->
                        EventListCard(event, onClick = { onEventClick(event.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun EventListCard(event: Event, onClick: () -> Unit) {
    val isOpen = event.status == EventStatus.OPEN
    ArxCard(onClick = onClick, variant = ArxCardVariant.Glass) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
        ) {
            ArxIconBadge(icon = ArxIcons.Event, style = ArxIconStyle.Soft)
            Column(Modifier.weight(1f)) {
                Text(event.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${formatDate(event.date)} • ${animalsLabel(event.animalCount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ArxStatusChip(
                text = if (isOpen) "Abierto" else "Cerrado",
                status = if (isOpen) ArxChipStatus.Success else ArxChipStatus.Neutral // ajusta a tus valores
            )
        }
    }
}