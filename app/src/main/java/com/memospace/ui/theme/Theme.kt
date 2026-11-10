package com.memospace.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.memospace.domain.model.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = MemoIndigoPrimary,
    onPrimary = MemoIndigoOnPrimary,
    primaryContainer = MemoIndigoPrimaryContainer,
    onPrimaryContainer = MemoIndigoOnPrimaryContainer,
    secondary = MemoSecondary,
    onSecondary = MemoOnSecondary,
    secondaryContainer = MemoSecondaryContainer,
    onSecondaryContainer = MemoOnSecondaryContainer,
    tertiary = MemoTertiary,
    onTertiary = MemoOnTertiary,
    tertiaryContainer = MemoTertiaryContainer,
    onTertiaryContainer = MemoOnTertiaryContainer,
    background = MemoBackgroundLight,
    onBackground = MemoOnBackgroundLight,
    surface = MemoSurfaceLight,
    onSurface = MemoOnSurfaceLight,
    surfaceVariant = MemoSurfaceVariantLight,
    onSurfaceVariant = MemoOnSurfaceVariantLight,
    outline = MemoOutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = MemoIndigoDarkPrimary,
    onPrimary = MemoIndigoDarkOnPrimary,
    primaryContainer = MemoIndigoDarkPrimaryContainer,
    onPrimaryContainer = MemoIndigoDarkOnPrimaryContainer,
    secondary = MemoSecondaryDark,
    onSecondary = MemoOnSecondaryDark,
    secondaryContainer = MemoSecondaryContainerDark,
    onSecondaryContainer = MemoOnSecondaryContainerDark,
    tertiary = MemoTertiaryDark,
    onTertiary = MemoOnTertiaryDark,
    tertiaryContainer = MemoTertiaryContainerDark,
    onTertiaryContainer = MemoOnTertiaryContainerDark,
    background = MemoBackgroundDark,
    onBackground = MemoOnBackgroundDark,
    surface = MemoSurfaceDark,
    onSurface = MemoOnSurfaceDark,
    surfaceVariant = MemoSurfaceVariantDark,
    onSurfaceVariant = MemoOnSurfaceVariantDark,
    outline = MemoOutlineDark
)

@Composable
fun MemoSpaceTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
