package com.example.checklist.Database.NotesDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void Insertnotes(NotesEntity notesEntity);

    @Query("SELECT * FROM Notes")
    List<NotesEntity> getall();

    @Query("SELECT * FROM Notes WHERE id = :id")
    NotesEntity getinfobyid(Long id);
}