package com.example.arrax.ui.screen.lobby.profile.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
//import com.example.arrax.core.model.AboutLink
import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.model.AlertChannel
import com.example.arrax.ui.screen.lobby.profile.settings.component.AboutLink
import com.example.arrax.ui.screen.lobby.profile.settings.component.SettingsGroupCard
import com.example.arrax.ui.screen.lobby.profile.settings.component.SettingsNavigationRow
import com.example.arrax.ui.screen.lobby.profile.settings.component.SettingsSectionHeader
import com.example.arrax.ui.screen.lobby.profile.settings.component.SettingsSwitchRow
import com.example.arrax.ui.screen.lobby.profile.settings.component.ThemeModeSelector
import com.example.arrax.ui.screen.lobby.profile.settings.component.subtitleRes
import com.example.arrax.ui.screen.lobby.profile.settings.component.titleRes
import com.example.arrax.ui.screen.lobby.settings.component.SettingsAboutSection

@Composable
fun LobbySettingsScreen(
    onBack: () -> Unit,
    onAboutLinkClick: (AboutLink) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LobbySettingsViewmodel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val appVersion = remember {
        val name = runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull()
        if (name != null) "v$name" else ""
    }

    LobbySettingsContent(
        state = state,
        appVersion = appVersion,
        onBack = onBack,
        onThemeModeChange = viewModel::onThemeModeChange,
        onAlertToggle = viewModel::onAlertToggle,
        onAboutLinkClick = onAboutLinkClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbySettingsContent(
    state: LobbySettingsState,
    appVersion: String,
    onBack: () -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onAlertToggle: (AlertChannel, Boolean) -> Unit,
    onAboutLinkClick: (AboutLink) -> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(decoration = ArxDecoration.None) {
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.common_settings), style = MaterialTheme.typography.headlineMedium) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
            ) {
                // 1. Apariencia
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader(stringResource(R.string.settings_section_appearance))
                    SettingsGroupCard {
                        ThemeModeSelector(
                            currentMode = state.themeMode,
                            onModeSelected = onThemeModeChange
                        )
                    }
                }

                // 2. Notificaciones
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader(stringResource(R.string.settings_section_alerts))
                    SettingsGroupCard {
                        val channels = AlertChannel.entries
                        channels.forEachIndexed { index, channel ->
                            SettingsSwitchRow(
                                title = stringResource(channel.titleRes),
                                subtitle = stringResource(channel.subtitleRes),
                                checked = state.alerts[channel] ?: true,
                                onCheckedChange = { onAlertToggle(channel, it) }
                            )
                            if (index != channels.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                            }
                        }
                    }
                }

                // 3. General
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader(stringResource(R.string.settings_section_general))
                    SettingsGroupCard {
                        SettingsNavigationRow(
                            title = stringResource(R.string.settings_language_title),
                            subtitle = stringResource(R.string.settings_language_subtitle)
                        )
                        HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                        SettingsNavigationRow(
                            title = stringResource(R.string.settings_sync_title),
                            subtitle = stringResource(
                                if (state.isOnline) R.string.settings_sync_online
                                else R.string.settings_sync_offline
                            )
                        )
                    }
                }

                // 4. Acerca de (al final)
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader(stringResource(R.string.settings_section_about))
                    SettingsAboutSection(version = appVersion, onLinkClick = onAboutLinkClick)
                }
            }
        }
    }
}

@Preview(name = "Light", widthDp = 360, heightDp = 900)
@Composable
private fun SettingsLightPreview() {
    ArxTheme(darkTheme = false) {
        LobbySettingsContent(LobbySettingsState(), "v1.0.0", {}, {}, { _, _ -> }, {})
    }
}

@Preview(name = "Dark - sin conexión", widthDp = 360, heightDp = 900)
@Composable
private fun SettingsDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbySettingsContent(
            LobbySettingsState(themeMode = ThemeMode.DARK, isOnline = false),
            "v1.0.0", {}, {}, { _, _ -> }, {}
        )
    }
}