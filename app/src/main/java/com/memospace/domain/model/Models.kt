package com.memospace.domain.model

/**
 * Distinguishes the fundamental content types supported by MemoSpace.
 */
enum class NoteType {
    NOTE,
    TASK,
    CHECKLIST
}

/**
 * Task priority levels: High, Medium, Low.
 */
enum class TaskPriority {
    HIGH,
    MEDIUM,
    LOW
}

/**
 * Appearance mode preference for MemoSpace.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

/**
 * Home workspace presentation layout.
 */
enum class LayoutMode {
    GRID,
    LIST
}

/**
 * Top quick-filter categories for the Home workspace.
 */
enum class HomeFilter {
    ALL,
    NOTES,
    TASKS,
    PINNED,
    TODAY
}

/**
 * Predefined note color options with light and dark mode hex definitions.
 */
enum class NoteColor(val colorName: String, val hexLight: Long, val hexDark: Long) {
    DEFAULT("Default", 0xFFFFFFFF, 0xFF1E1F24),
    RED("Red", 0xFFFFEBEE, 0xFF3E2723),
    ORANGE("Orange", 0xFFFFF3E0, 0xFF4E342E),
    YELLOW("Yellow", 0xFFFFFDE7, 0xFF4A442D),
    GREEN("Green", 0xFFE8F5E9, 0xFF1B382B),
    BLUE("Blue", 0xFFE3F2FD, 0xFF1A2A3A),
    PURPLE("Purple", 0xFFF3E5F5, 0xFF2E1C38),
    PINK("Pink", 0xFFFCE4EC, 0xFF3E1E2B),
    GRAY("Gray", 0xFFF5F5F5, 0xFF2A2B2E),
    DARK("Dark", 0xFFECEFF1, 0xFF121316)
}

/**
 * Domain entity representing a category.
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String? = null,
    val color: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Domain entity representing a label (hashtag-based or manual).
 */
data class Label(
    val id: Long = 0,
    val name: String
)

/**
 * Domain entity for individual checklist items.
 */
data class ChecklistItem(
    val id: Long = 0,
    val noteId: Long = 0,
    val text: String,
    val isCompleted: Boolean = false,
    val sortOrder: Int = 0
)

/**
 * Core unified MemoSpace item representation for UI rendering and state management.
 */
data class MemoItem(
    val id: Long = 0,
    val title: String,
    val content: String,
    val type: NoteType = NoteType.NOTE,
    val color: NoteColor = NoteColor.DEFAULT,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val labels: List<String> = emptyList(),
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val reminderAt: Long? = null,

    // Task-specific metadata
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: Long? = null,
    val completedAt: Long? = null,

    // Checklist preview
    val checklistItems: List<ChecklistItem> = emptyList(),

    // Image attachments preview
    val imagePaths: List<String> = emptyList()
)
