package com.eliezer.noteapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eliezer.noteapp.model.Note;
import com.eliezer.noteapp.model.NoteRepositoryWithExecutor;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepositoryWithExecutor repository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepositoryWithExecutor(application);
        syncNotes();
        allNotes = syncNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }


    public LiveData<List<Note>> getAllNotes() {
        return repository.getNotesDao();
    }

    private  LiveData<List<Note>> syncNotes() {

        LiveData<List<Note>> dblist = repository.getNotesDao();
        LiveData<List<Note>> firebaselist = repository.getNotesFirebase();
        if (dblist.getValue().isEmpty() && !firebaselist.getValue().isEmpty()) {
            for (Note note : firebaselist.getValue()) {
                repository.insertDao(note);
            }
        } else if (!dblist.getValue().isEmpty() && firebaselist.getValue().isEmpty()) {
            for (Note note : dblist.getValue()) {
                repository.insertFirebase(note);
            }
        }
        return  repository.getNotesDao();
     }
}

