package com.memospace.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.memospace.data.local.dao.NoteDao
import com.memospace.data.local.entity.CategoryEntity
import com.memospace.data.local.entity.ChecklistItemEntity
import com.memospace.data.local.entity.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
        ChecklistItemEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java,
                    "memospace.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
