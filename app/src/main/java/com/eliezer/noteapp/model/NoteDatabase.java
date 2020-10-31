package com.eliezer.noteapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static volatile NoteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

   static NoteDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "note_database")
                            .addCallback(roomCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
         }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                NoteDao noteDao = INSTANCE.noteDao();
                noteDao.deleteAllNotes();
                noteDao.insert(new Note("Title 1", "Description 1", 1));
                noteDao.insert(new Note("Title 2", "Description 2", 2));
                noteDao.insert(new Note("Title 3", "Description 3", 3));

            });
//            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private NoteDao noteDao;
//        private PopulateDbAsyncTask(NoteDatabase db) {
//            noteDao = db.noteDao();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            noteDao.insert(new Note("Title 1", "Description 1", 1));
//            noteDao.insert(new Note("Title 2", "Description 2", 2));
//            noteDao.insert(new Note("Title 3", "Description 3", 3));
//            return null;
//        }
//    }
}
