package com.memospace.data.repository

import com.memospace.data.local.dao.NoteDao
import com.memospace.data.local.entity.ChecklistItemEntity
import com.memospace.data.local.entity.NoteEntity
import com.memospace.domain.model.ChecklistItem
import com.memospace.domain.model.MemoItem
import com.memospace.domain.model.NoteColor
import com.memospace.domain.model.NoteType
import com.memospace.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val noteDao: NoteDao) {

    fun getActiveNotes(): Flow<List<MemoItem>> {
        return noteDao.getAllActiveNotes().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getArchivedNotes(): Flow<List<MemoItem>> {
        return noteDao.getArchivedNotes().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getTrashNotes(): Flow<List<MemoItem>> {
        return noteDao.getTrashNotes().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getPinnedNotes(): Flow<List<MemoItem>> {
        return noteDao.getPinnedNotes().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getNoteById(id: Long): MemoItem? {
        val entity = noteDao.getNoteById(id) ?: return null
        val checklist = noteDao.getChecklistItemsSync(id).map { it.toDomain() }
        return entity.toDomain(checklist = checklist)
    }

    fun getNoteByIdFlow(id: Long): Flow<MemoItem?> {
        return noteDao.getNoteByIdFlow(id).map { it?.toDomain() }
    }

    suspend fun saveNote(item: MemoItem): Long {
        val entity = item.toEntity().copy(updatedAt = System.currentTimeMillis())
        val id = if (entity.id == 0L) {
            noteDao.insertNote(entity)
        } else {
            noteDao.updateNote(entity)
            entity.id
        }

        // Save checklist items if present
        if (item.type == NoteType.CHECKLIST) {
            noteDao.deleteChecklistItemsForNote(id)
            item.checklistItems.forEachIndexed { index, checkItem ->
                noteDao.insertChecklistItem(
                    ChecklistItemEntity(
                        noteId = id,
                        text = checkItem.text,
                        isCompleted = checkItem.isCompleted,
                        sortOrder = index
                    )
                )
            }
        }
        return id
    }

    suspend fun togglePin(id: Long) {
        val note = noteDao.getNoteById(id) ?: return
        noteDao.updateNote(note.copy(isPinned = !note.isPinned, updatedAt = System.currentTimeMillis()))
    }

    suspend fun toggleArchive(id: Long) {
        val note = noteDao.getNoteById(id) ?: return
        noteDao.updateNote(note.copy(isArchived = !note.isArchived, isPinned = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun moveToTrash(id: Long) {
        val note = noteDao.getNoteById(id) ?: return
        noteDao.updateNote(note.copy(isDeleted = true, isPinned = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun restoreFromTrash(id: Long) {
        val note = noteDao.getNoteById(id) ?: return
        noteDao.updateNote(note.copy(isDeleted = false, updatedAt = System.currentTimeMillis()))
    }

    suspend fun deletePermanently(id: Long) {
        noteDao.deleteNotePermanently(id)
    }

    suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }

    suspend fun toggleTaskCompletion(id: Long) {
        val note = noteDao.getNoteById(id) ?: return
        val newCompleted = !note.isCompleted
        val completedAt = if (newCompleted) System.currentTimeMillis() else null
        noteDao.updateNote(
            note.copy(
                isCompleted = newCompleted,
                completedAt = completedAt,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    private fun NoteEntity.toDomain(checklist: List<ChecklistItem> = emptyList()): MemoItem {
        return MemoItem(
            id = id,
            title = title,
            content = content,
            type = runCatching { NoteType.valueOf(type) }.getOrDefault(NoteType.NOTE),
            color = runCatching { NoteColor.valueOf(color) }.getOrDefault(NoteColor.DEFAULT),
            categoryId = categoryId,
            isPinned = isPinned,
            isArchived = isArchived,
            isDeleted = isDeleted,
            createdAt = createdAt,
            updatedAt = updatedAt,
            reminderAt = reminderAt,
            isCompleted = isCompleted,
            priority = runCatching { TaskPriority.valueOf(priority) }.getOrDefault(TaskPriority.MEDIUM),
            dueDate = dueDate,
            completedAt = completedAt,
            checklistItems = checklist
        )
    }

    private fun MemoItem.toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            type = type.name,
            color = color.name,
            categoryId = categoryId,
            isPinned = isPinned,
            isArchived = isArchived,
            isDeleted = isDeleted,
            createdAt = createdAt,
            updatedAt = updatedAt,
            reminderAt = reminderAt,
            isCompleted = isCompleted,
            priority = priority.name,
            dueDate = dueDate,
            completedAt = completedAt
        )
    }

    private fun ChecklistItemEntity.toDomain(): ChecklistItem {
        return ChecklistItem(
            id = id,
            noteId = noteId,
            text = text,
            isCompleted = isCompleted,
            sortOrder = sortOrder
        )
    }
}
