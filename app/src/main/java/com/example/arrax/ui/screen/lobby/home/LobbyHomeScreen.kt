package com.example.arrax.ui.screen.lobby.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.R
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.display.ArxLoading
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.lobby.home.component.BusinessListItem
import com.example.arrax.ui.screen.lobby.home.component.LobbyEmptyBusinesses
import com.example.arrax.ui.screen.lobby.home.component.LobbyTopBar
import com.example.arrax.ui.screen.lobby.home.component.RecentHeader
import com.example.arrax.ui.screen.lobby.home.component.WorkspaceCard

@Composable
fun LobbyHomeScreen(
    onBusinessClick: (String) -> Unit,
    onCreateBusinessClick: () -> Unit,
    onJoinClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: LobbyHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ArxBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LobbyTopBar(
                    userName = state.userName,
                    unreadNotifications = state.unreadNotifications,
                    onNotificationsClick = onNotificationsClick,
                    onSettingsClick = onSettingsClick,
                    onProfileClick = onProfileClick
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                if (state.isLoading) {
                    ArxLoading(message = stringResource(R.string.lobby_loading))
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (state.isOffline) OfflineBadge()
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(ArxSpacing.md)
                        ) {
                            item {
                                Column(modifier = Modifier.padding(bottom = ArxSpacing.lg)) {
                                    Text(
                                        text = stringResource(R.string.lobby_greeting, state.userName),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = stringResource(R.string.lobby_greeting_subtitle),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            item {
                                WorkspaceCard(
                                    onCreateBusinessClick = onCreateBusinessClick,
                                    onJoinClick = onJoinClick,
                                    modifier = Modifier.padding(bottom = ArxSpacing.xl)
                                )
                            }
                            item {
                                RecentHeader(
                                    onViewAllClick = onViewAllClick,
                                    modifier = Modifier.padding(bottom = ArxSpacing.sm)
                                )
                            }
                            if (state.recentBusinesses.isEmpty()) {
                                item {
                                    LobbyEmptyBusinesses(
                                        onCreateBusinessClick = onCreateBusinessClick,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                items(state.recentBusinesses, key = { it.id }) { business ->
                                    BusinessListItem(
                                        business = business,
                                        onClick = { onBusinessClick(business.id) },
                                        modifier = Modifier.padding(bottom = ArxSpacing.sm)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfflineBadge() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.lobby_offline_banner),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs)
        )
    }
}
