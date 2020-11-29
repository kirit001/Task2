package com.example.checklist.Database.NotesDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.checklist.Database.UserDatabase.UserEntity;

@Database(entities = {NotesEntity.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String dbName = "Notes";
    private static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getNotesDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(context, NotesDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return notesDatabase;
    }

    public abstract NotesDao notesDao();
}

