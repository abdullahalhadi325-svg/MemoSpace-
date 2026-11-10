package com.memospace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.memospace.domain.model.NoteColor
import com.memospace.domain.model.NoteType
import com.memospace.domain.model.TaskPriority

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val type: String = NoteType.NOTE.name,
    val color: String = NoteColor.DEFAULT.name,
    val categoryId: Long? = null,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val reminderAt: Long? = null,

    // Task & checklist specific fields
    val isCompleted: Boolean = false,
    val priority: String = TaskPriority.MEDIUM.name,
    val dueDate: Long? = null,
    val completedAt: Long? = null
)
