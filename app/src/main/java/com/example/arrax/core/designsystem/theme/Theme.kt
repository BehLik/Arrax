package com.example.arrax.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// -----------------------------------------------------------------------------
// Extended colors: success / warning / info + extras (disabled, glass, gradientes)
// -----------------------------------------------------------------------------

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Immutable
data class ExtendedColorScheme(
    val success: ColorFamily,
    val warning: ColorFamily,
    val info: ColorFamily,
    val disabled: Color,
    val glass: Color,
    val glassBorder: Color,
    val primaryGradient: Brush,
    val secondaryGradient: Brush
)

private val unspecifiedFamily = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

private val LocalExtendedColorScheme = staticCompositionLocalOf {
    ExtendedColorScheme(
        success = unspecifiedFamily,
        warning = unspecifiedFamily,
        info = unspecifiedFamily,
        disabled = Color.Unspecified,
        glass = Color.Unspecified,
        glassBorder = Color.Unspecified,
        primaryGradient = Brush.horizontalGradient(listOf(Color.Unspecified, Color.Unspecified)),
        secondaryGradient = Brush.horizontalGradient(listOf(Color.Unspecified, Color.Unspecified))
    )
}

val MaterialTheme.extendedColors: ExtendedColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColorScheme.current

private val extendedLight = ExtendedColorScheme(
    success = ColorFamily(successLight, onSuccessLight, successContainerLight, onSuccessContainerLight),
    warning = ColorFamily(warningLight, onWarningLight, warningContainerLight, onWarningContainerLight),
    info = ColorFamily(infoLight, onInfoLight, infoContainerLight, onInfoContainerLight),
    disabled = disabledLight,
    glass = ArxGlass.Light,
    glassBorder = ArxGlass.BorderLight,
    primaryGradient = ArxGradients.PrimaryLight,
    secondaryGradient = ArxGradients.SecondaryLight
)

private val extendedDark = ExtendedColorScheme(
    success = ColorFamily(successDark, onSuccessDark, successContainerDark, onSuccessContainerDark),
    warning = ColorFamily(warningDark, onWarningDark, warningContainerDark, onWarningContainerDark),
    info = ColorFamily(infoDark, onInfoDark, infoContainerDark, onInfoContainerDark),
    disabled = disabledDark,
    glass = ArxGlass.Dark,
    glassBorder = ArxGlass.BorderDark,
    primaryGradient = ArxGradients.PrimaryDark,
    secondaryGradient = ArxGradients.SecondaryDark
)

// -----------------------------------------------------------------------------
// LIGHT
// -----------------------------------------------------------------------------

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight
)

private val lightMediumContrastScheme = lightScheme.copy(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast
)

private val lightHighContrastScheme = lightScheme.copy(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast
)

// -----------------------------------------------------------------------------
// DARK
// -----------------------------------------------------------------------------

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark
)

private val darkMediumContrastScheme = darkScheme.copy(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast
)

private val darkHighContrastScheme = darkScheme.copy(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast
)

// -----------------------------------------------------------------------------
// ArxTheme
// -----------------------------------------------------------------------------

enum class ArxContrast { Standard, Medium, High }

@Composable
fun ArxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    contrast: ArxContrast = ArxContrast.Standard,
    // false por defecto: el color dinámico de Android pisaría la paleta de marca
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> when (contrast) {
            ArxContrast.Standard -> darkScheme
            ArxContrast.Medium -> darkMediumContrastScheme
            ArxContrast.High -> darkHighContrastScheme
        }

        else -> when (contrast) {
            ArxContrast.Standard -> lightScheme
            ArxContrast.Medium -> lightMediumContrastScheme
            ArxContrast.High -> lightHighContrastScheme
        }
    }

    CompositionLocalProvider(
        LocalExtendedColorScheme provides if (darkTheme) extendedDark else extendedLight
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ArxTypography,
            shapes = ArxShapes,
            content = content
        )
    }
}