package com.example.arrax.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// -----------------------------------------------------------------------------
// Extended semantic colors
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
    val info: ColorFamily
)

private val LocalExtendedColorScheme =
    staticCompositionLocalOf {
        ExtendedColorScheme(
            success = ColorFamily(
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified
            ),
            warning = ColorFamily(
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified
            ),
            info = ColorFamily(
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified
            )
        )
    }

val MaterialTheme.extendedColors: ExtendedColorScheme
    @Composable
    get() = LocalExtendedColorScheme.current

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

// -----------------------------------------------------------------------------
// LIGHT — Medium contrast
// -----------------------------------------------------------------------------

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

// -----------------------------------------------------------------------------
// LIGHT — High contrast
// -----------------------------------------------------------------------------

private val lightHighContrastScheme = lightMediumContrastScheme.copy(
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

// -----------------------------------------------------------------------------
// DARK — Medium contrast
// -----------------------------------------------------------------------------

private val darkMediumContrastScheme = darkScheme.copy(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,

    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,

    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryDarkMediumContrast,
    onTertiaryContainer = onTertiaryDarkMediumContrast,

    onBackground = onBackgroundDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast
)

// -----------------------------------------------------------------------------
// DARK — High contrast
// -----------------------------------------------------------------------------

private val darkHighContrastScheme = darkMediumContrastScheme.copy(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,

    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,

    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    onTertiaryContainer = onTertiaryDarkHighContrast,

    onBackground = onBackgroundDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineDarkHighContrast
)

// -----------------------------------------------------------------------------
// Extended semantic schemes
// -----------------------------------------------------------------------------

private val lightExtendedColors = ExtendedColorScheme(
    success = ColorFamily(
        color = successLight,
        onColor = onSuccessLight,
        colorContainer = successContainerLight,
        onColorContainer = onSuccessContainerLight
    ),
    warning = ColorFamily(
        color = warningLight,
        onColor = onWarningLight,
        colorContainer = warningContainerLight,
        onColorContainer = onWarningContainerLight
    ),
    info = ColorFamily(
        color = infoLight,
        onColor = onInfoLight,
        colorContainer = infoContainerLight,
        onColorContainer = onInfoContainerLight
    )
)

private val darkExtendedColors = ExtendedColorScheme(
    success = ColorFamily(
        color = successDark,
        onColor = onSuccessDark,
        colorContainer = successContainerDark,
        onColorContainer = onSuccessContainerDark
    ),
    warning = ColorFamily(
        color = warningDark,
        onColor = onWarningDark,
        colorContainer = warningContainerDark,
        onColorContainer = onWarningContainerDark
    ),
    info = ColorFamily(
        color = infoDark,
        onColor = onInfoDark,
        colorContainer = infoContainerDark,
        onColorContainer = onInfoContainerDark
    )
)


@Composable
fun ArxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val useDynamicColors = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        useDynamicColors && darkTheme ->
            dynamicDarkColorScheme(context)

        useDynamicColors ->
            dynamicLightColorScheme(context)

        darkTheme ->
            darkScheme

        else ->
            lightScheme
    }

    val extendedColors = when {
        darkTheme -> darkExtendedColors
        else -> lightExtendedColors
    }

    androidx.compose.runtime.CompositionLocalProvider(
        LocalExtendedColorScheme provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = ArxShapes,
            content = content
        )
    }
}