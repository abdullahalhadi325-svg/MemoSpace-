package com.memospace.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memospace.data.repository.NoteRepository
import com.memospace.domain.model.ChecklistItem
import com.memospace.domain.model.MemoItem
import com.memospace.domain.model.NoteColor
import com.memospace.domain.model.NoteType
import com.memospace.domain.model.TaskPriority
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditorUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val type: NoteType = NoteType.NOTE,
    val color: NoteColor = NoteColor.DEFAULT,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val isColorPickerOpen: Boolean = false
)

class NoteEditorViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var autoSaveJob: Job? = null

    fun loadNote(noteId: Long, initialType: NoteType) {
        if (noteId > 0) {
            viewModelScope.launch {
                val item = repository.getNoteById(noteId)
                if (item != null) {
                    _uiState.value = NoteEditorUiState(
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        type = item.type,
                        color = item.color,
                        isPinned = item.isPinned,
                        isArchived = item.isArchived,
                        priority = item.priority,
                        isCompleted = item.isCompleted,
                        checklistItems = item.checklistItems
                    )
                }
            }
        } else {
            _uiState.update { it.copy(type = initialType) }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
        scheduleAutoSave()
    }

    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent) }
        scheduleAutoSave()
    }

    fun onColorSelected(newColor: NoteColor) {
        _uiState.update { it.copy(color = newColor, isColorPickerOpen = false) }
        scheduleAutoSave()
    }

    fun onTogglePin() {
        _uiState.update { it.copy(isPinned = !it.isPinned) }
        scheduleAutoSave()
    }

    fun onToggleArchive() {
        _uiState.update { it.copy(isArchived = !it.isArchived) }
        scheduleAutoSave()
    }

    fun onToggleColorPicker() {
        _uiState.update { it.copy(isColorPickerOpen = !it.isColorPickerOpen) }
    }

    fun onAddChecklistItem(text: String) {
        if (text.isBlank()) return
        val current = _uiState.value.checklistItems.toMutableList()
        current.add(ChecklistItem(text = text, isCompleted = false, sortOrder = current.size))
        _uiState.update { it.copy(checklistItems = current) }
        scheduleAutoSave()
    }

    fun onToggleChecklistItem(index: Int) {
        val current = _uiState.value.checklistItems.toMutableList()
        if (index in current.indices) {
            val item = current[index]
            current[index] = item.copy(isCompleted = !item.isCompleted)
            _uiState.update { it.copy(checklistItems = current) }
            scheduleAutoSave()
        }
    }

    fun onDeleteChecklistItem(index: Int) {
        val current = _uiState.value.checklistItems.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _uiState.update { it.copy(checklistItems = current) }
            scheduleAutoSave()
        }
    }

    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(500) // Debounce auto-save by 500ms
            saveImmediately()
        }
    }

    fun saveImmediately() {
        val state = _uiState.value
        // Only save if there's actual content or title
        if (state.title.isBlank() && state.content.isBlank() && state.checklistItems.isEmpty() && state.id == 0L) {
            return
        }

        viewModelScope.launch {
            val savedId = repository.saveNote(
                MemoItem(
                    id = state.id,
                    title = state.title,
                    content = state.content,
                    type = state.type,
                    color = state.color,
                    isPinned = state.isPinned,
                    isArchived = state.isArchived,
                    priority = state.priority,
                    isCompleted = state.isCompleted,
                    checklistItems = state.checklistItems
                )
            )
            if (state.id == 0L && savedId > 0) {
                _uiState.update { it.copy(id = savedId) }
            }
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        val id = _uiState.value.id
        if (id > 0) {
            viewModelScope.launch {
                repository.moveToTrash(id)
                onDeleted()
            }
        } else {
            onDeleted()
        }
    }
}
