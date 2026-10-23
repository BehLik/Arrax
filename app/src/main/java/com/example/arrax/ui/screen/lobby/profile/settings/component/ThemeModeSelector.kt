package com.example.arrax.ui.screen.lobby.profile.settings.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.arrax.R
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.model.ThemeMode

@Composable
fun ThemeModeSelector(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado real del teléfono, para saber qué heredar al apagar "Modo Sistema".
    val isSystemDark = isSystemInDarkTheme()

    Column(modifier = modifier.fillMaxWidth()) {
        SettingsSwitchRow(
            title = stringResource(R.string.lobby_configuration_ModeSelectorTitle),
            subtitle = stringResource(R.string.lobby_configutation_ModeSelectorSubTitle),
            iconRes = ArxIcons.System,
            checked = currentMode == ThemeMode.SYSTEM,
            onCheckedChange = { checked ->
                if (checked) {
                    onModeSelected(ThemeMode.SYSTEM)
                } else {
                    onModeSelected(if (isSystemDark) ThemeMode.DARK else ThemeMode.LIGHT)
                }
            }
        )

        AnimatedVisibility(
            visible = currentMode != ThemeMode.SYSTEM,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                SettingsSwitchRow(
                    title = stringResource(R.string.lobby_configuration_Modelight_Title),
                    subtitle = stringResource(R.string.lobby_configuration_Modelight_SubTitle),
                    iconRes = ArxIcons.LightMode,
                    checked = currentMode == ThemeMode.LIGHT,
                    onCheckedChange = { checked ->
                        onModeSelected(if (checked) ThemeMode.LIGHT else ThemeMode.DARK)
                    }
                )
                SettingsSwitchRow(
                    title = stringResource(R.string.lobby_configutation_ModeDark_Title),
                    subtitle = stringResource(R.string.lobby_configuration_ModeDark_SubTitle),
                    iconRes = ArxIcons.DarkMode,
                    checked = currentMode == ThemeMode.DARK,
                    onCheckedChange = { checked ->
                        onModeSelected(if (checked) ThemeMode.DARK else ThemeMode.LIGHT)
                    }
                )
            }
        }
    }
}