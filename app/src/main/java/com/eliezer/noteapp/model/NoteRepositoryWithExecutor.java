package com.eliezer.noteapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NoteRepositoryWithExecutor {
    private NoteDao mNoteDao = null;
    private MutableLiveData<List<Note>> mAllNotesDao;
    private MutableLiveData<List<Note>> mAllNotesFirebase;

    private DatabaseReference myRef=null;

    private MutableLiveData<Boolean> success;
    public NoteRepositoryWithExecutor(Application application) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("notes");

        NoteDatabase db = NoteDatabase.getInstance(application);
        mNoteDao = db.noteDao();

        getAllnotes();
        getAllNotesFirebase();
    }

    private void getAllnotes() {
        getAllNotesFirebase();
        getAllNotesDao();

    }

    private void getAllNotesFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllNotesDao() {
        mAllNotesDao = new MutableLiveData(mNoteDao.getAllNotes());
    }

    public LiveData<Boolean> getStatus() {
        return success;
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotesDao;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Note note) {
        insertDao(note);
        insertFirebase(note);
    }

    private void insertFirebase(Note note) {
        myRef.child(note.getSsn()).setValue(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        success.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success.setValue(false);
                    }
                });
    }

    public void update(Note note) {
        updateDao(note);
        insertFirebase(note);
    }

    public void delete(Note note) {
        deleteDao(note);
        deleteFirebase(note);
    }

    private void deleteFirebase(Note note) {

    }

    public void deleteAllNotes() {
        deleteAllNotesDao();
    }

    private void insertDao(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(note);
        });
    }


    private void updateDao(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.update(note);
        });
    }

    private void deleteDao(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.delete(note);
        });
    }


    private void deleteAllNotesDao() {
        NoteDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteAllNotes();
        });
    }
}
