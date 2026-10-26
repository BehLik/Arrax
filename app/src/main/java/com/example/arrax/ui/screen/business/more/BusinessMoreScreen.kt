package com.example.arrax.ui.screen.business.more

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.backgrounds.ArxScreenSurface
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.ui.screen.business.more.component.LeaveBusinessButton
import com.example.arrax.ui.screen.business.more.component.MoreSection
import com.example.arrax.ui.screen.business.more.component.adminOptions
import com.example.arrax.ui.screen.business.more.component.infoOptions

@Composable
fun BusinessMoreScreen(        
    modifier: Modifier = Modifier,
    onTeamClick: () -> Unit = {},
    onCatalogClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLeaveBusinessClick: () -> Unit = {},
    viewModel: BusinessMoreViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BusinessMoreContent(
        state = state,
        onTeamClick = onTeamClick,
        onCatalogClick = onCatalogClick,
        onHistoryClick = onHistoryClick,
        onSettingsClick = onSettingsClick,
        onHelpClick = onHelpClick,
        onAboutClick = onAboutClick,
        onLeaveBusinessClick = onLeaveBusinessClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessMoreContent(
    state: BusinessMoreState,
    onTeamClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLeaveBusinessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(modifier = modifier, decoration = ArxDecoration.None) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArxTopBar(title = "Más", centeredTitle = false)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ArxSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg)
            ) {
                MoreSection(
                    title = "Administración del negocio",
                    options = adminOptions(onTeamClick, onCatalogClick, onHistoryClick, onSettingsClick)
                )
                MoreSection(
                    title = "Información",
                    options = infoOptions(state.appVersion, onHelpClick, onAboutClick)
                )
            }

            LeaveBusinessButton(onClick = onLeaveBusinessClick)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Preview(
    showBackground = true, widthDp = 360, heightDp = 780,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BusinessMorePreview() {
    ArxTheme(darkTheme = isSystemInDarkTheme()) {
        BusinessMoreContent(
            state = BusinessMoreState(appVersion = "1.0.0"),
            onTeamClick = {}, onCatalogClick = {}, onHistoryClick = {}, onSettingsClick = {},
            onHelpClick = {}, onAboutClick = {}, onLeaveBusinessClick = {}
        )
    }
}