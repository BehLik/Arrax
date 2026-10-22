package com.example.arrax.ui.screen.lobby.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.brand.ArxLogo
import com.example.arrax.core.designsystem.components.brand.ArxLogoLayout
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.ui.screen.lobby.profile.component.ProfileGroupCard
import com.example.arrax.ui.screen.lobby.profile.component.ProfileHeader
import com.example.arrax.ui.screen.lobby.profile.component.ProfileLogoutRow
import com.example.arrax.ui.screen.lobby.profile.component.ProfileOption
import com.example.arrax.ui.screen.lobby.profile.component.ProfileOptionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyProfileScreen(
    userName: String,
    userEmail: String,
    activeBusinessName: String?,
    onBack: () -> Unit,
    onOptionClick: (ProfileOption) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArxBackground(decoration = ArxDecoration.Mountains) {
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("Mi perfil", style = MaterialTheme.typography.headlineMedium) },
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
                ProfileHeader(
                    name = userName,
                    email = userEmail,
                    modifier = Modifier.fillMaxWidth()
                )

                ProfileGroupCard {
                    val options = ProfileOption.entries
                    options.forEachIndexed { index, option ->
                        ProfileOptionRow(
                            title = option.title,
                            subtitle = if (option == ProfileOption.ActiveBusiness) activeBusinessName else null,
                            onClick = { onOptionClick(option) }
                        )
                        if (index != options.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.extendedColors.glassBorder)
                        }
                    }
                }

                ProfileGroupCard {
                    ProfileLogoutRow(onClick = onLogout)
                }
            }
        }
    }
}

@Preview(name = "Light", widthDp = 360, heightDp = 720)
@Composable
private fun ProfileLightPreview() {
    ArxTheme(darkTheme = false) {
        LobbyProfileScreen("Admin", "admin@arrax.com", "Rancho El Progreso", {}, {}, {})
    }
}

@Preview(name = "Dark", widthDp = 360, heightDp = 720)
@Composable
private fun ProfileDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyProfileScreen("Admin", "admin@arrax.com", "Rancho El Progreso", {}, {}, {})
    }
}