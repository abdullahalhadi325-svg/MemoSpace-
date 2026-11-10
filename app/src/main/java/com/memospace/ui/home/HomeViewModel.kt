package com.memospace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memospace.data.repository.NoteRepository
import com.memospace.domain.model.HomeFilter
import com.memospace.domain.model.LayoutMode
import com.memospace.domain.model.MemoItem
import com.memospace.domain.model.NoteType
import com.memospace.domain.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for the Home screen and workspace.
 */
data class HomeUiState(
    val searchQuery: String = "",
    val selectedFilter: HomeFilter = HomeFilter.ALL,
    val layoutMode: LayoutMode = LayoutMode.GRID,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isFabExpanded: Boolean = false,
    val items: List<MemoItem> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * ViewModel managing the Home workspace state, filters, search, layout, and theme.
 */
class HomeViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(HomeFilter.ALL)
    private val _layoutMode = MutableStateFlow(LayoutMode.GRID)
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    private val _isFabExpanded = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getActiveNotes(),
        _searchQuery,
        _selectedFilter,
        _layoutMode,
        _themeMode,
        _isFabExpanded
    ) { allNotes, query, filter, layout, theme, fabExpanded ->
        val filtered = allNotes.filter { note ->
            // Filter by search query
            val matchesQuery = query.isBlank() ||
                    note.title.contains(query, ignoreCase = true) ||
                    note.content.contains(query, ignoreCase = true)

            // Filter by tab/chip
            val matchesFilter = when (filter) {
                HomeFilter.ALL -> true
                HomeFilter.NOTES -> note.type == NoteType.NOTE
                HomeFilter.TASKS -> note.type == NoteType.TASK
                HomeFilter.PINNED -> note.isPinned
                HomeFilter.TODAY -> {
                    val now = System.currentTimeMillis()
                    val oneDay = 24 * 60 * 60 * 1000L
                    (now - note.updatedAt) < oneDay
                }
            }
            matchesQuery && matchesFilter
        }

        HomeUiState(
            searchQuery = query,
            selectedFilter = filter,
            layoutMode = layout,
            themeMode = theme,
            isFabExpanded = fabExpanded,
            items = filtered,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: HomeFilter) {
        _selectedFilter.value = filter
    }

    fun onToggleLayoutMode() {
        _layoutMode.update { current ->
            if (current == LayoutMode.GRID) LayoutMode.LIST else LayoutMode.GRID
        }
    }

    fun onToggleFab() {
        _isFabExpanded.update { !it }
    }

    fun onDismissFab() {
        _isFabExpanded.value = false
    }

    fun onSetThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun onTogglePin(noteId: Long) {
        viewModelScope.launch {
            repository.togglePin(noteId)
        }
    }

    fun onToggleTaskCompletion(noteId: Long) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(noteId)
        }
    }
}
