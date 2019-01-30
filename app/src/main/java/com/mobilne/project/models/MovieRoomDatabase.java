package com.mobilne.project.models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    //    private static MovieRoomDatabase INSTANCE = Room.databaseBuilder(context, MovieRoomDatabase.class, "movie_database");
    private static volatile MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
//                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static MovieRoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MovieDao mDao;

        PopulateDbAsync(MovieRoomDatabase db) {
            mDao = db.movieDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
//            Word word = new Word("Hello");
//            mDao.insert(word);
//            word = new Word("World");
//            mDao.insert(word);
            return null;
        }
    }
}