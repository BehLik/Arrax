package com.example.arrax.ui.screen.lobby.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.display.ArxEmptyState
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun LobbyEmptyBusinesses(onCreateBusinessClick: () -> Unit, modifier: Modifier = Modifier) {
    ArxEmptyState(
        title = stringResource(R.string.lobby_empty_title),
        description = stringResource(R.string.lobby_empty_description),
        iconRes = ArxIcons.Box,
        actionText = stringResource(R.string.lobby_empty_action),
        onActionClick = onCreateBusinessClick,
        modifier = modifier
    )
}
