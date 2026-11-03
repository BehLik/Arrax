package com.example.arrax.ui.screen.business.more.subscreen.catalog

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.cards.ArxCatalogGridCard
import com.example.arrax.core.designsystem.components.inputs.ArxSearchField
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarAction
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.cards.ArxCatalogMenuAction
import com.example.arrax.core.designsystem.components.dialogs.ArxConfirmDialog
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.model.CutOrigin
import com.example.arrax.ui.screen.business.more.subscreen.catalog.component.CutEditorSheet
import com.example.arrax.core.designsystem.components.cards.ArxCatalogCardVariant
import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
/**
 * Pantalla de catálogo de cortes.
 * Por ahora usa DefaultCuts (funciona sin internet); luego se conecta al ViewModel/Firestore.
 */
@Composable
fun BusinessCatalogScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: BusinessCatalogViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val actions = remember(viewModel, onBackClick) {
        BusinessCatalogActions(
            onBackClick = onBackClick,
            onQueryChange = viewModel::onQueryChange,
            onToggleView = viewModel::onToggleView,
            onAddClick = viewModel::onAddClick,
            onEditClick = viewModel::onEditClick,
            onToggleActive = viewModel::onToggleActive,
            onDeleteRequest = viewModel::onDeleteRequest,
            onDeleteConfirm = viewModel::onDeleteConfirm,
            onDeleteDismiss = viewModel::onDeleteDismiss,
            onResetRequest = viewModel::onResetRequest,
            onResetConfirm = viewModel::onResetConfirm,
            onResetDismiss = viewModel::onResetDismiss,
            onEditorChange = viewModel::onEditorChange,
            onEditorSave = viewModel::onEditorSave,
            onEditorDismiss = viewModel::onEditorDismiss,
            onMessageShown = viewModel::onMessageShown,
        )
    }
    BusinessCatalogContent(state = state, actions = actions, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessCatalogContent(
    state: BusinessCatalogState,
    actions: BusinessCatalogActions,
    modifier: Modifier = Modifier,
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let { snackbar.showSnackbar(it); actions.onMessageShown() }
    }

    ArxBackground(modifier, decoration = ArxDecoration.None) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                ArxTopBar(
                    title = "Cortes",
                    subtitle = "Administra el catálogo de cortes disponibles para tus eventos.",
                    centeredTitle = false,
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = actions.onBackClick,
                    actions = {
                        ArxTopBarAction(
                            iconRes = if (state.isGridView) ArxIcons.ViewList else ArxIcons.GridView,
                            contentDescription = if (state.isGridView) "Cambiar a vista de lista"
                            else "Cambiar a vista de cuadrícula",
                            onClick = actions.onToggleView,
                        )
                    },
                )

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                ) {
                    ArxSearchField(
                        query = state.query,
                        onQueryChange = actions.onQueryChange,
                        placeholder = "Buscar corte...",
                    )

                    if (state.visibleCuts.isEmpty() && !state.isLoading) {
                        Text(
                            text = "No hay cortes que coincidan con \"${state.query}\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = ArxSpacing.md),
                        )
                    } else {
                        AnimatedContent(
                            targetState = state.isGridView,
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            transitionSpec = {
                                (fadeIn(tween(180)) + scaleIn(initialScale = 0.98f, animationSpec = tween(180))) togetherWith
                                        fadeOut(tween(100))
                            },
                            label = "CatalogViewTransition",
                        ) { gridView ->
                            val padding = PaddingValues(top = 4.dp, bottom = 96.dp)
                            if (gridView) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = padding,
                                ) {
                                    items(state.visibleCuts, key = { it.id }) { cut ->
                                        CutCard(cut, ArxCatalogCardVariant.Grid, actions)
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = padding,
                                ) {
                                    items(state.visibleCuts, key = { it.id }) { cut ->
                                        CutCard(cut, ArxCatalogCardVariant.List, actions, Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = actions.onAddClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(ArxSpacing.md),
            ) { Icon(Icons.Filled.Add, contentDescription = "Agregar corte") }

            SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
        }
    }

    state.editor?.let {
        CutEditorSheet(it, actions.onEditorChange, actions.onEditorSave, actions.onEditorDismiss)
    }
    state.cutToDelete?.let {
        ArxConfirmDialog(
            title = "Eliminar corte",
            message = "¿Eliminar \"${it.name}\" de tu catálogo? Los eventos anteriores conservan su nombre.",
            confirmLabel = "Eliminar", destructive = true,
            onConfirm = actions.onDeleteConfirm, onDismiss = actions.onDeleteDismiss,
        )
    }
    state.cutToReset?.let {
        ArxConfirmDialog(
            title = "Restablecer corte",
            message = "\"${it.name}\" volverá al nombre, estado y descripción originales del sistema.",
            confirmLabel = "Restablecer",
            onConfirm = actions.onResetConfirm, onDismiss = actions.onResetDismiss,
        )
    }
}
@Composable
private fun CutCard(
    cut: CutCatalog,
    variant: ArxCatalogCardVariant,
    actions: BusinessCatalogActions,
    modifier: Modifier = Modifier,
) {
    ArxCatalogGridCard(
        title = cut.name,
        subtitle = cut.state.label + if (!cut.active) " · Oculto" else "",
        tag = when (cut.origin) {
            CutOrigin.DEFAULT -> "Sistema"
            CutOrigin.MODIFIED -> "Modificado"
            CutOrigin.CUSTOM -> "Personalizado"
        },
        image = cut.imageRes?.let { ArxCatalogImage.Resource(it) }
            ?: cut.imageUrl?.let { ArxCatalogImage.Remote(it) }
            ?: ArxCatalogImage.None,
        variant = variant,
        muted = !cut.active,
        onClick = { actions.onEditClick(cut) },
        menuActions = buildList {
            add(ArxCatalogMenuAction("Editar") { actions.onEditClick(cut) })
            add(ArxCatalogMenuAction(if (cut.active) "Ocultar" else "Mostrar") { actions.onToggleActive(cut) })
            if (cut.origin == CutOrigin.MODIFIED) {
                add(ArxCatalogMenuAction("Restablecer original") { actions.onResetRequest(cut) })
            }
            if (cut.origin == CutOrigin.CUSTOM) {
                add(ArxCatalogMenuAction("Eliminar", destructive = true) { actions.onDeleteRequest(cut) })
            }
        },
        modifier = modifier,
    )
}
@Composable
private fun CatalogHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
