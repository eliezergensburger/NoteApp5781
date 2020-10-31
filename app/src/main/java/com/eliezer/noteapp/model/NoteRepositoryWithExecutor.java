package com.eliezer.noteapp.model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteRepositoryWithExecutor {
    private NoteDao mNoteDao;
    private MutableLiveData<List<Note>> mAllNotes;

    public NoteRepositoryWithExecutor(Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application);
        mNoteDao = db.noteDao();
        mAllNotes = new MutableLiveData(mNoteDao.getAllNotes());
    }

    public  LiveData<List<Note>> getAllNotes(){
        return mAllNotes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(note);
        });
    }
     public void update(Note note) {
         NoteDatabase.databaseWriteExecutor.execute(() -> {
             mNoteDao.update(note);
         });
     }
    public void delete(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.delete(note);
        });
    }
    public void deleteAllNotes() {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteAllNotes();
        });
    }
}
