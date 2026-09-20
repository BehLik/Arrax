package com.example.arrax.core.designsystem.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArxTopBar(
    title: String,
    modifier: Modifier = Modifier,
    centeredTitle: Boolean = true,
    navigationType: ArxTopBarNavigationType = ArxTopBarNavigationType.NONE,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
        actionIconContentColor = MaterialTheme.colorScheme.primary
    )

    val navigationIcon: @Composable () -> Unit = {
        when (navigationType) {
            ArxTopBarNavigationType.BACK -> {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // Puedes cambiarlo a ArxIcons.ChevronLeft
                        contentDescription = "Volver"
                    )
                }
            }
            ArxTopBarNavigationType.MENU -> {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.Menu, // Puedes cambiarlo a ArxIcons.Menu
                        contentDescription = "Menú principal"
                    )
                }
            }
            ArxTopBarNavigationType.NONE -> {}
        }
    }

    val titleContent: @Composable () -> Unit = {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (centeredTitle) {
        CenterAlignedTopAppBar(
            title = titleContent,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors
        )
    } else {
        TopAppBar(
            title = titleContent,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = colors
        )
    }
}

enum class ArxTopBarNavigationType {
    NONE, BACK, MENU
}

@Preview(showBackground = true)
@Composable
fun ArxTopBarPreview() {
    ArxTheme {
        ArxTopBar(
            title = "Extracción de Cortes",
            navigationType = ArxTopBarNavigationType.BACK,
            centeredTitle = true
        )
    }
}