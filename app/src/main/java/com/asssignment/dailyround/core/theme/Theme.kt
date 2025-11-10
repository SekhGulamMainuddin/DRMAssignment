package com.asssignment.dailyround.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = BlueSecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = OnBackgroundDark,
    onSurface = OnBackgroundDark,
    error = WrongRed,
    tertiary = CorrectGreen
)

private val LightColorScheme = lightColorScheme(
    primary = YellowPrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = BlueSecondaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = OnBackgroundLight,
    onSurface = OnBackgroundLight,
    error = WrongRed,
    tertiary = CorrectGreen
)

@Composable
fun DailyRoundAsssignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
