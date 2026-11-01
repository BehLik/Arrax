package com.example.arrax.ui.screen.lobby.profile.settings.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.arrax.R
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.domain.model.AlertChannel

@Composable
fun AlertsSection(
    masterEnabled: Boolean,
    alerts: Map<AlertChannel, Boolean>,
    onMasterToggle: (Boolean) -> Unit,
    onAlertToggle: (AlertChannel, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)) {
        SettingsSectionHeader(stringResource(R.string.settings_section_alerts))
        SettingsGroupCard {
            SettingsSwitchRow(
                title = stringResource(R.string.settings_alert_master_title),
                subtitle = stringResource(R.string.settings_alert_master_subtitle),
                iconRes = ArxIcons.Notification, // ajusta al nombre real en ArxIcons
                checked = masterEnabled,
                onCheckedChange = onMasterToggle
            )
            AnimatedVisibility(
                visible = masterEnabled,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    AlertChannel.entries.forEach { channel ->
                        HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                        SettingsSwitchRow(
                            title = stringResource(channel.titleRes),
                            subtitle = stringResource(channel.subtitleRes),
                            checked = alerts[channel] ?: true,
                            onCheckedChange = { onAlertToggle(channel, it) }
                        )
                    }
                }
            }
        }
    }
}