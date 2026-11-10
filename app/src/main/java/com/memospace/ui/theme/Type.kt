package com.memospace.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Standard SansSerif font family.
 * On Android, this resolves to Roboto for Latin characters and seamlessly
 * falls back to Noto Sans Bengali for Unicode/Bangla text.
 */
val MemoFontFamily = FontFamily.SansSerif

/**
 * MemoSpace typography according to design targets:
 * - Large page title: 24sp
 * - Section/title: 20–22sp
 * - Note title: 20–22sp
 * - Main body: 16sp
 * - Secondary text: 14sp
 * - Metadata: 12sp
 * - Button/action text: 14sp
 */
val Typography = Typography(
    // Large page title (24sp)
    headlineLarge = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp
    ),
    // Section / dialog title (22sp)
    headlineMedium = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Note title (20sp)
    titleLarge = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    // Section header / sub-card title (18sp)
    titleMedium = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    // Small title (16sp)
    titleSmall = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    // Main note body text (16sp)
    bodyLarge = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp
    ),
    // Secondary text / subtitles (14sp)
    bodyMedium = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    ),
    // Small body text (12sp)
    bodySmall = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),
    // Button & interactive action text (14sp)
    labelLarge = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // Chips & badges (12sp)
    labelMedium = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),
    // Metadata (timestamp, counter, status labels) (12sp)
    labelSmall = TextStyle(
        fontFamily = MemoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp
    )
)
