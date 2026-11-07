package com.example.arrax.ui.root

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.core.designsystem.theme.ArxTheme
import com.example.arrax.domain.model.ThemeMode
import com.example.arrax.ui.root.navigation.ArraxNavHost
import com.example.arrax.ui.root.theme.AppThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val themeVm: AppThemeViewModel = hiltViewModel()

            val themeMode by themeVm.themeMode.collectAsState()

            val isSystemDark = isSystemInDarkTheme()

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            val view = LocalView.current

            SideEffect {
                val controller = WindowInsetsControllerCompat(
                    window,
                    view
                )

                // true: iconos oscuros.
                // false: iconos claros.
                val useDarkIcons = !darkTheme

                controller.isAppearanceLightStatusBars = useDarkIcons
                controller.isAppearanceLightNavigationBars = useDarkIcons
            }

            ArxTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArraxNavHost()
                }
            }
        }
    }
}