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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.cards.ArxCatalogCardVariant
import com.example.arrax.core.designsystem.components.cards.ArxCatalogGridCard
import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
import com.example.arrax.core.designsystem.components.inputs.ArxSearchField
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarAction
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarNavigationType
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.model.CutOption
import com.example.arrax.core.model.DefaultCuts
import com.google.api.Context

/**
 * Pantalla de catálogo de cortes.
 * Por ahora usa DefaultCuts (funciona sin internet); luego se conecta al ViewModel/Firestore.
 */
@Composable
fun BusinessCatalogScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val cuts = remember { DefaultCuts }

    BusinessCatalogContent(
        cuts = cuts,
        query = query,
        onQueryChange = { query = it },
        onBackClick = onBackClick,
        modifier = modifier,
        onEditClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessCatalogContent(
    cuts: List<CutOption>,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddCutClick: () -> Unit = {},
    onCutClick: (CutOption) -> Unit = {},
    onCutMenuClick: (CutOption) -> Unit = {},
) {
    var isGridView by rememberSaveable { mutableStateOf(true) }
    val visibleCuts = remember(cuts, query) {
        if (query.isBlank()) cuts else cuts.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }

    ArxBackground(modifier, decoration = ArxDecoration.None) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                ArxTopBar(
                    title = "Cortes",
                    subtitle = "Administra el catálogo de cortes disponibles para tus eventos.",
                    centeredTitle = false,
                    navigationType = ArxTopBarNavigationType.BACK,
                    onNavigationClick = onBackClick,
                    actions = {
                        ArxTopBarAction(
                            iconRes = if (isGridView) {
                                ArxIcons.ViewList
                            } else {
                                ArxIcons.GridView
                            },
                            contentDescription = if (isGridView) {
                                "Cambiar a vista de lista"
                            } else {
                                "Cambiar a vista de cuadrícula"
                            },
                            onClick = {
                                isGridView = !isGridView
                            }
                        )

                        ArxTopBarAction(
                            iconRes = ArxIcons.Edit,
                            contentDescription = "Editar",
                            onClick = onEditClick
                        )
                    },
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = ArxSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
                ) {
//                    CatalogHeader(
//                        title = "",
//                        subtitle = "Administra el catálogo de cortes disponibles para tus eventos.",
//                    )

                    ArxSearchField(
                        query = query,
                        onQueryChange = onQueryChange,
                        placeholder = "Buscar corte...",
                    )

                    if (visibleCuts.isEmpty()) {
                        Text(
                            text = "No hay cortes que coincidan con \"$query\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = ArxSpacing.md)
                        )
                    } else {
                        if (isGridView) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    top = 4.dp,
                                    bottom = 96.dp
                                )
                            ) {
                                items(
                                    items = visibleCuts,
                                    key = { it.id }
                                ) { cut ->
                                    ArxCatalogGridCard(
                                        title = cut.name,
                                        image = cut.imageRes
                                            ?.let { ArxCatalogImage.Resource(it) }
                                            ?: ArxCatalogImage.None,
                                        variant = ArxCatalogCardVariant.Grid,
                                        onClick = { onCutClick(cut) },
                                        onMenuClick = { onCutMenuClick(cut) }
                                    )
                                }
                            }
                        } else {
                            androidx.compose.foundation.lazy.LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    top = 4.dp,
                                    bottom = 96.dp
                                )
                            ) {
                                items(
                                    items = visibleCuts,
                                    key = { it.id }
                                ) { cut ->
                                    ArxCatalogGridCard(
                                        title = cut.name,
                                        image = cut.imageRes
                                            ?.let { ArxCatalogImage.Resource(it) }
                                            ?: ArxCatalogImage.None,
                                        variant = ArxCatalogCardVariant.List,
                                        onClick = { onCutClick(cut) },
                                        onMenuClick = { onCutMenuClick(cut) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Si ya tienes ArxFab, cámbialo aquí.
            FloatingActionButton(
                onClick = onAddCutClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(ArxSpacing.md),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar corte")
            }
        }
    }
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

@Preview(showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessCatalogScreenPreview() {
    ArxTheme {
        BusinessCatalogContent(
            cuts = DefaultCuts,
            query = "",
            onQueryChange = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun BusinessCatalogScreenSearchPreview() {
    ArxTheme {
        BusinessCatalogContent(
            cuts = DefaultCuts,
            query = "co",
            onQueryChange = {},
        )
    }
}