package com.example.arrax.ui.screen.lobby.join

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.arrax.core.model.JoinMethod
import com.example.arrax.ui.screen.lobby.join.component.JoinCodeForm
import com.example.arrax.ui.screen.lobby.join.component.JoinMethodSelector
import com.example.arrax.ui.screen.lobby.join.component.JoinQrScannerView
import com.example.arrax.ui.screen.lobby.join.component.JoinQrShortcutCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyJoinScreen(
    method: JoinMethod,
    code: String,
    isLoading: Boolean,
    errorMessage: String?,
    torchOn: Boolean,
    onBack: () -> Unit,
    onMethodChange: (JoinMethod) -> Unit,
    onCodeChange: (String) -> Unit,
    onJoin: () -> Unit,
    onTorchToggle: () -> Unit,
    modifier: Modifier = Modifier,
    /** Aquí se inyecta el preview de CameraX cuando se conecte el escáner. */
    cameraContent: @Composable () -> Unit = {}
) {
    ArxBackground(decoration = ArxDecoration.Mountains) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (method == JoinMethod.CODE) "Unirse a negocio" else "Escanear QR",
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
                    .fillMaxSize()
                    .padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.lg)
            ) {
                JoinMethodSelector(
                    selected = method,
                    onSelected = onMethodChange,
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedContent(
                    targetState = method,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier.weight(1f),
                    label = "join_method"
                ) { current ->
                    when (current) {
                        JoinMethod.CODE -> Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(ArxSpacing.xl)
                        ) {
                            JoinQrShortcutCard(onClick = { onMethodChange(JoinMethod.QR) })
                            JoinCodeForm(
                                code = code,
                                isLoading = isLoading,
                                errorMessage = errorMessage,
                                onCodeChange = onCodeChange,
                                onJoin = onJoin
                            )
                        }

                        JoinMethod.QR -> JoinQrScannerView(
                            torchOn = torchOn,
                            errorMessage = errorMessage,
                            onTorchToggle = onTorchToggle,
                            modifier = Modifier.fillMaxSize(),
                            cameraContent = cameraContent
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Código - Light", widthDp = 360, heightDp = 720)
@Composable
private fun JoinCodeLightPreview() {
    ArxTheme(darkTheme = false) {
        LobbyJoinScreen(JoinMethod.CODE, "", false, null, false, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Código - Dark (error)", widthDp = 360, heightDp = 720)
@Composable
private fun JoinCodeDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyJoinScreen(JoinMethod.CODE, "ABC123", false, "Código inválido o expirado", false, {}, {}, {}, {}, {})
    }
}

@Preview(name = "QR - Dark", widthDp = 360, heightDp = 720)
@Composable
private fun JoinQrDarkPreview() {
    ArxTheme(darkTheme = true) {
        LobbyJoinScreen(JoinMethod.QR, "", false, null, false, {}, {}, {}, {}, {})
    }
}