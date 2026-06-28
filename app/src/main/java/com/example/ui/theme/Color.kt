package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrandRedLight = Color(0xFF8B0000)
val BackgroundThemeLight = Color(0xFFFDFCF5)
val TextPrimaryLight = Color(0xFF1C1B1F)
val TextSecondaryLight = Color(0xFF79747E)
val TextTitleLight = Color(0xFF484644)
val BorderColorLight = Color(0xFFE6E1D9)
val CardBackgroundLight = Color(0xFFF3EFE3)
val AccentGoldLight = Color(0xFFC5A059)
val NavBarBackgroundLight = Color(0xFFF7F2E9)

// Dark theme fallbacks
val BackgroundDark = Color(0xFF1A1A1A)
val SurfaceDark = Color(0xFF2A2A2A)
val SurfaceVariantDark = Color(0xFF333333)
val TextDark = Color(0xFFE0E0E0)
val BorderColorDark = Color(0xFF444444)

// Dynamic properties
val BrandRed: Color
    @Composable get() = MaterialTheme.colorScheme.primary

val BackgroundTheme: Color
    @Composable get() = MaterialTheme.colorScheme.background

val TextPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

val TextSecondary: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

val TextTitle: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

val BorderColor: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary

val CardBackground: Color
    @Composable get() = MaterialTheme.colorScheme.surface

val AccentGold: Color
    @Composable get() = MaterialTheme.colorScheme.secondary

val NavBarBackground: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceContainer
