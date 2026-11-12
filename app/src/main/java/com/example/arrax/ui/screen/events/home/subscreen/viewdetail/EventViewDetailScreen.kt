package com.example.arrax.ui.screen.events.home.subscreen.viewdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.components.display.ArxSectionHeader
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.events.home.component.EventEditPill
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.component.EventDetailCutRow
import com.example.arrax.ui.screen.events.home.subscreen.viewdetail.component.EventDetailInfoCard


@Composable
fun EventViewDetailScreen(
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewDetailViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventViewDetailContent(state, onBack, onEditClick, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventViewDetailContent(
    state: EventViewDetailState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            ArxTopBar(
                title = "Detalle del evento",
                centeredTitle = false,
                navigationType = ArxTopBarNavigationType.BACK,
                onNavigationClick = onBackClick,
                actions = {
                    if (state.canEdit) {
                        EventEditPill(
                            onClick = onEditClick,
                            modifier = Modifier.padding(end = ArxSpacing.sm)
                        )
                    }
                }
            )

            if (state.isLoading) {
                ArxLoading()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = ArxSpacing.md, end = ArxSpacing.md,
                        top = ArxSpacing.sm, bottom = ArxSpacing.md
                    ),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.md)
                ) {
                    item(key = "info") { EventDetailInfoCard(state) }
                    item(key = "cuts_header") {
                        ArxSectionHeader(title = "Cortes (${state.cuts.size})")
                    }
                    items(state.cuts, key = { it.cutId }) { cut -> EventDetailCutRow(cut) }
                }
            }
        }
    }
}