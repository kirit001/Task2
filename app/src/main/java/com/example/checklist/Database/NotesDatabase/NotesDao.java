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

    @Query("DELETE FROM Notes WHERE id = :id")
    void deleteById(Integer id);

    @Query("UPDATE Notes SET title = :title, notes = :notes WHERE id = :id")
    int editinfo(Integer id, String title, String notes);
}