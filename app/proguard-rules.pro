# Proguard rules for MemoSpace
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static abstract ** createOpenHelper(androidx.sqlite.db.SupportSQLiteOpenHelper$Configuration);
}
