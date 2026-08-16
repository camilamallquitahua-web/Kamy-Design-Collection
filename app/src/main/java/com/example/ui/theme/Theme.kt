package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFFFF8B7A),
  onPrimary = Color(0xFF5F140D),
  primaryContainer = TerracottaDark,
  onPrimaryContainer = TerracottaContainer,
  secondary = Color(0xFFBAC3FF),
  onSecondary = Color(0xFF1E2850),
  secondaryContainer = IndigoSecondary,
  onSecondaryContainer = IndigoContainer,
  tertiary = AmberWarm,
  background = DarkBg,
  onBackground = Color(0xFFEDEDF2),
  surface = DarkSurface,
  onSurface = Color(0xFFEDEDF2),
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = Color(0xFFCAC8D4),
  outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
  primary = TerracottaPrimary,
  onPrimary = Color.White,
  primaryContainer = TerracottaContainer,
  onPrimaryContainer = OnTerracottaContainer,
  secondary = IndigoSecondary,
  onSecondary = Color.White,
  secondaryContainer = IndigoContainer,
  onSecondaryContainer = OnIndigoContainer,
  tertiary = AmberWarm,
  background = SandSurface,
  onBackground = Color(0xFF1B1C20),
  surface = Color.White,
  onSurface = Color(0xFF1B1C20),
  surfaceVariant = SandSurfaceVariant,
  onSurfaceVariant = Color(0xFF5C5B64),
  outline = CardBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our intentional curated luxury palette
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
