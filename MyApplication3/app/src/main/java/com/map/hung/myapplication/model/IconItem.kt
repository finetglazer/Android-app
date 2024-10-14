package com.map.hung.myapplication.model

data class IconItem(
    val parentIcon: Int,  // Main icon
    val subIcon: Int?,    // Sub-icon (can be null)
    val label: String,    // Main label
    val subLabel: String? = null  // Sub-label (optional, can be null)
)
