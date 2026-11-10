package com.memospace.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.memospace.R
import com.memospace.domain.model.LayoutMode
import com.memospace.domain.model.MemoItem
import com.memospace.ui.components.EmptyStateView
import com.memospace.ui.components.MemoSpaceFAB
import com.memospace.ui.components.MemoSpaceTopBar
import com.memospace.ui.components.QuickFilterRow
import com.memospace.ui.home.components.NoteCard

/**
 * Main Home workspace screen of MemoSpace with Grid & List layouts and real data support.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenDrawer: () -> Unit,
    onNoteClick: (MemoItem) -> Unit,
    onCreateNote: () -> Unit,
    onCreateTask: () -> Unit,
    onCreateChecklist: () -> Unit,
    onCreateImageNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column {
                MemoSpaceTopBar(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    layoutMode = uiState.layoutMode,
                    onToggleLayoutMode = viewModel::onToggleLayoutMode,
                    onMenuClick = onOpenDrawer
                )
                QuickFilterRow(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = viewModel::onFilterSelected
                )
            }
        },
        floatingActionButton = {
            MemoSpaceFAB(
                isExpanded = uiState.isFabExpanded,
                onToggleExpand = viewModel::onToggleFab,
                onCreateNote = {
                    viewModel.onDismissFab()
                    onCreateNote()
                },
                onCreateTask = {
                    viewModel.onDismissFab()
                    onCreateTask()
                },
                onCreateChecklist = {
                    viewModel.onDismissFab()
                    onCreateChecklist()
                },
                onCreateImageNote = {
                    viewModel.onDismissFab()
                    onCreateImageNote()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.items.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Outlined.EditNote,
                    title = stringResource(id = R.string.empty_home_title),
                    subtitle = stringResource(id = R.string.empty_home_subtitle),
                    actionLabel = "+ ${stringResource(id = R.string.create_note)}",
                    onActionClick = onCreateNote,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (uiState.layoutMode == LayoutMode.GRID) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalItemSpacing = 10.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.items, key = { it.id }) { item ->
                            NoteCard(
                                item = item,
                                onClick = { onNoteClick(item) },
                                onTogglePin = { viewModel.onTogglePin(item.id) },
                                onToggleCompletion = { viewModel.onToggleTaskCompletion(item.id) }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.items, key = { it.id }) { item ->
                            NoteCard(
                                item = item,
                                onClick = { onNoteClick(item) },
                                onTogglePin = { viewModel.onTogglePin(item.id) },
                                onToggleCompletion = { viewModel.onToggleTaskCompletion(item.id) }
                            )
                        }
                    }
                }
            }

            // Scrim overlay when FAB is expanded (dismisses FAB on tap outside)
            AnimatedVisibility(
                visible = uiState.isFabExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = viewModel::onDismissFab
                        )
                )
            }
        }
    }
}
