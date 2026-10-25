package com.example.arrax.ui.screen.lobby.settings

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.lobby.settings.component.AboutLink
import com.example.arrax.ui.screen.lobby.settings.component.SettingsAboutSection
import com.example.arrax.ui.screen.lobby.settings.component.SettingsGroupCard
import com.example.arrax.ui.screen.lobby.settings.component.SettingsNavigationRow
import com.example.arrax.ui.screen.lobby.settings.component.SettingsPreference
import com.example.arrax.ui.screen.lobby.settings.component.SettingsSectionHeader
import com.example.arrax.ui.screen.lobby.settings.component.SettingsSwitchRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbySettingsScreen(
    alertStates: Map<AlertChannel, Boolean>,
    appVersion: String,
    onBack: () -> Unit,
    onPreferenceClick: (SettingsPreference) -> Unit,
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
                    title = {
                        Text(
                            "Configuración",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader("Acerca de Arrax")
                    SettingsAboutSection(
                        version = appVersion,
                        onLinkClick = onAboutLinkClick
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader("Preferencias generales")
                    SettingsGroupCard {
                        val prefs = SettingsPreference.entries
                        prefs.forEachIndexed { index, pref ->
                            SettingsNavigationRow(
                                title = pref.title,
                                subtitle = pref.subtitle,
                                onClick = { onPreferenceClick(pref) }
                            )
                            if (index != prefs.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
                    SettingsSectionHeader("Gestión de alertas")
                    SettingsGroupCard {
                        val channels = AlertChannel.entries
                        channels.forEachIndexed { index, channel ->
                            SettingsSwitchRow(
                                title = channel.title,
                                subtitle = channel.subtitle,
                                checked = alertStates[channel] ?: false,
                                onCheckedChange = { onAlertToggle(channel, it) }
                            )
                            if (index != channels.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                            }
                        }
                    }
                }
            }

        }
    }
}

private val previewAlerts = AlertChannel.entries.associateWith { true }

@Preview(name = "Light", widthDp = 360, heightDp = 900)
@Composable
private fun SettingsLightPreview() {
    ArxTheme(darkTheme = false) {
        LobbySettingsScreen(previewAlerts, "v1.0.0", {}, {}, { _, _ -> }, {})
    }
}

@Preview(name = "Dark", widthDp = 360, heightDp = 900)
@Composable
private fun SettingsDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbySettingsScreen(previewAlerts, "v1.0.0", {}, {}, { _, _ -> }, {})
    }
}