package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PeriwinklePurple80,
    onPrimary = Color(0xFF1E1A66),
    primaryContainer = PeriwinklePurpleDark,
    onPrimaryContainer = Color(0xFFE2E0FF),
    secondary = MintGreen80,
    onSecondary = Color(0xFF003825),
    secondaryContainer = Color(0xFF005138),
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = CoralPink80,
    background = SurfaceDark,
    surface = Color(0xFF1A1C29),
    surfaceVariant = Color(0xFF26293D),
    onSurface = Color(0xFFE3E4EF),
    onSurfaceVariant = Color(0xFFC7C9D8)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPeriwinkle,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainerPeriwinkle,
    onPrimaryContainer = OnPrimaryContainerPeriwinkle,
    secondary = SecondaryMint,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainerMint,
    onSecondaryContainer = OnSecondaryContainerMint,
    tertiary = TertiaryCoral,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerCoral,
    onTertiaryContainer = OnTertiaryContainerCoral,
    background = SurfaceLight,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F2F9),
    onBackground = Color(0xFF1E202E),
    onSurface = Color(0xFF1E202E),
    onSurfaceVariant = Color(0xFF5B5F75),
    outline = Color(0xFFD6D8E5),
    outlineVariant = Color(0xFFE5E7F2),
    error = ErrorRed,
    errorContainer = ErrorContainerRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
