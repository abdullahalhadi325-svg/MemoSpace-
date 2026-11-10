package com.memospace.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.memospace.R

/**
 * Top-level destinations of MemoSpace.
 */
sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : Screen(
        route = "home",
        titleRes = R.string.nav_home,
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object Notes : Screen(
        route = "notes",
        titleRes = R.string.nav_notes,
        unselectedIcon = Icons.Outlined.Description,
        selectedIcon = Icons.Filled.Description
    )

    object Tasks : Screen(
        route = "tasks",
        titleRes = R.string.nav_tasks,
        unselectedIcon = Icons.Outlined.CheckCircle,
        selectedIcon = Icons.Filled.CheckCircle
    )

    object Categories : Screen(
        route = "categories",
        titleRes = R.string.nav_categories,
        unselectedIcon = Icons.Outlined.Folder,
        selectedIcon = Icons.Filled.Folder
    )

    object Pinned : Screen(
        route = "pinned",
        titleRes = R.string.nav_pinned,
        unselectedIcon = Icons.Outlined.PushPin,
        selectedIcon = Icons.Filled.PushPin
    )

    object Archive : Screen(
        route = "archive",
        titleRes = R.string.nav_archive,
        unselectedIcon = Icons.Outlined.Archive,
        selectedIcon = Icons.Filled.Archive
    )

    object Trash : Screen(
        route = "trash",
        titleRes = R.string.nav_trash,
        unselectedIcon = Icons.Outlined.Delete,
        selectedIcon = Icons.Filled.Delete
    )

    object Settings : Screen(
        route = "settings",
        titleRes = R.string.nav_settings,
        unselectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )

    companion object {
        val primaryDestinations = listOf(Home, Notes, Tasks, Categories, Pinned)
        val secondaryDestinations = listOf(Archive, Trash, Settings)
        val allDestinations = primaryDestinations + secondaryDestinations
    }
}
