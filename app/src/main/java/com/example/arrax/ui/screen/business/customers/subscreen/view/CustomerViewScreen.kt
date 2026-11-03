package com.example.arrax.ui.screen.business.customers.subscreen.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.buttons.ArxSecondaryButton
import com.example.arrax.core.designsystem.components.buttons.ArxTextButton
import com.example.arrax.core.designsystem.components.dialogs.ArxConfirmDialog
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.subscreen.view.component.CustomerContactSection
import com.example.arrax.ui.screen.business.customers.subscreen.view.component.CustomerHeader
import com.example.arrax.ui.screen.business.customers.subscreen.view.component.CustomerOrdersSection

@Composable
fun CustomerViewScreen(
    onBack: () -> Unit,
    onEditClick: (customerId: String) -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomerViewViewmodel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val customer = state.customer

    val actions = remember(viewModel, onBack, customer, context) {
        CustomerViewActions(
            onBack = onBack,
            onEditClick = { customer?.let { onEditClick(it.id) } },
            onCallClick = { customer?.let { context.dial(it.phone) } },
            onMapsClick = { customer?.let { context.openMaps(it.address) } },
            onDeleteRequest = viewModel::onDeleteRequest,
            onDeleteConfirm = viewModel::onDeleteConfirm,
            onDeleteDismiss = viewModel::onDeleteDismiss,
            onMessageShown = viewModel::onMessageShown,
        )
    }

    LaunchedEffect(state.deleted) { if (state.deleted) onDeleted() }

    CustomerViewContent(state, actions, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CustomerViewContent(
    state: CustomerViewState,
    actions: CustomerViewActions,
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
                    title = "Detalle de cliente",
                    centeredTitle = false,
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = actions.onBack,
                )

                val customer = state.customer
                when {
                    state.isLoading -> Box(Modifier.weight(1f).fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }

                    customer == null -> Box(Modifier.weight(1f).fillMaxSize(), Alignment.Center) {
                        Text(
                            "No se encontró el cliente.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    else -> Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                        verticalArrangement = Arrangement.spacedBy(ArxSpacing.md),
                    ) {
                        CustomerHeader(customer)
                        CustomerContactSection(customer, actions.onCallClick, actions.onMapsClick)
                        CustomerOrdersSection()
                        ArxSecondaryButton(text = "Editar", onClick = actions.onEditClick)
                        ArxTextButton(
                            text = "Eliminar cliente",
                            onClick = actions.onDeleteRequest,
                            isDestructive = true,
                            enabled = !state.isDeleting,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }
                }
            }
            SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
        }
    }

    if (state.showDeleteDialog && state.customer != null) {
        ArxConfirmDialog(
            title = "Eliminar cliente",
            message = "¿Estás seguro de eliminar a ${state.customer.name}? " +
                    "Los pedidos anteriores se mantendrán en el historial.",
            confirmLabel = "Eliminar",
            destructive = true,
            onConfirm = actions.onDeleteConfirm,
            onDismiss = actions.onDeleteDismiss,
        )
    }
}

private fun Context.dial(phone: String) {
    try {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(phone)}")))
    } catch (_: ActivityNotFoundException) { }
}

private fun Context.openMaps(address: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(address)}")))
    } catch (_: ActivityNotFoundException) { }
}