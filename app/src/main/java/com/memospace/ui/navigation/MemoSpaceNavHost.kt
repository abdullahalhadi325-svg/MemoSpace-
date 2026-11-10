package com.memospace.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.memospace.R
import com.memospace.data.local.MemoDatabase
import com.memospace.data.repository.NoteRepository
import com.memospace.domain.model.NoteType
import com.memospace.ui.components.EmptyStateView
import com.memospace.ui.components.MemoSpaceDrawerContent
import com.memospace.ui.editor.NoteEditorScreen
import com.memospace.ui.editor.NoteEditorViewModel
import com.memospace.ui.home.HomeScreen
import com.memospace.ui.home.HomeViewModel
import kotlinx.coroutines.launch

/**
 * Top-level application navigation container with ModalNavigationDrawer and NavHost.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoSpaceApp(
    repository: NoteRepository,
    homeViewModel: HomeViewModel,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute == Screen.Home.route,
        drawerContent = {
            ModalDrawerSheet {
                MemoSpaceDrawerContent(
                    currentRoute = currentRoute,
                    onNavigateToDestination = { screen ->
                        scope.launch { drawerState.close() }
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNoteClick = { item ->
                        navController.navigate("note_editor/${item.id}?type=${item.type.name}")
                    },
                    onCreateNote = {
                        navController.navigate("note_editor/0?type=${NoteType.NOTE.name}")
                    },
                    onCreateTask = {
                        navController.navigate("note_editor/0?type=${NoteType.TASK.name}")
                    },
                    onCreateChecklist = {
                        navController.navigate("note_editor/0?type=${NoteType.CHECKLIST.name}")
                    },
                    onCreateImageNote = {
                        navController.navigate("note_editor/0?type=${NoteType.NOTE.name}")
                    }
                )
            }

            composable(
                route = "note_editor/{noteId}?type={type}",
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.LongType
                        defaultValue = 0L
                    },
                    navArgument("type") {
                        type = NavType.StringType
                        defaultValue = NoteType.NOTE.name
                    }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                val typeStr = backStackEntry.arguments?.getString("type") ?: NoteType.NOTE.name
                val noteType = runCatching { NoteType.valueOf(typeStr) }.getOrDefault(NoteType.NOTE)

                val editorViewModel = remember(noteId) {
                    NoteEditorViewModel(repository).apply {
                        loadNote(noteId, noteType)
                    }
                }

                NoteEditorScreen(
                    viewModel = editorViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Notes.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_notes),
                    emptyTitle = stringResource(id = R.string.empty_notes_title),
                    emptySubtitle = stringResource(id = R.string.empty_notes_subtitle),
                    emptyIcon = Icons.Outlined.Description,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Tasks.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_tasks),
                    emptyTitle = stringResource(id = R.string.empty_tasks_title),
                    emptySubtitle = stringResource(id = R.string.empty_tasks_subtitle),
                    emptyIcon = Icons.Outlined.CheckCircle,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Categories.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_categories),
                    emptyTitle = stringResource(id = R.string.nav_categories),
                    emptySubtitle = "Organize notes and tasks into custom categories.",
                    emptyIcon = Icons.Outlined.Folder,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Pinned.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_pinned),
                    emptyTitle = stringResource(id = R.string.empty_pinned_title),
                    emptySubtitle = stringResource(id = R.string.empty_pinned_subtitle),
                    emptyIcon = Icons.Outlined.PushPin,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Archive.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_archive),
                    emptyTitle = stringResource(id = R.string.empty_archive_title),
                    emptySubtitle = stringResource(id = R.string.empty_archive_subtitle),
                    emptyIcon = Icons.Outlined.Archive,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Trash.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_trash),
                    emptyTitle = stringResource(id = R.string.empty_trash_title),
                    emptySubtitle = stringResource(id = R.string.empty_trash_subtitle),
                    emptyIcon = Icons.Outlined.Delete,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(Screen.Settings.route) {
                DestinationPlaceholderScreen(
                    title = stringResource(id = R.string.nav_settings),
                    emptyTitle = stringResource(id = R.string.nav_settings),
                    emptySubtitle = "Manage appearance, notifications, view mode, and data.",
                    emptyIcon = Icons.Outlined.Settings,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DestinationPlaceholderScreen(
    title: String,
    emptyTitle: String,
    emptySubtitle: String,
    emptyIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.open_navigation_drawer)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EmptyStateView(
                icon = emptyIcon,
                title = emptyTitle,
                subtitle = emptySubtitle,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
