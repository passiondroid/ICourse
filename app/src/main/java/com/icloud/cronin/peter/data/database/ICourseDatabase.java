package com.icloud.cronin.peter.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.icloud.cronin.peter.data.model.RaceLocation;
import com.icloud.cronin.peter.data.model.RaceCourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * Created by Arif Khan on 20/10/18.
 */

@Database(entities = {RaceCourse.class, RaceLocation.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class ICourseDatabase extends RoomDatabase {

    private static ICourseDatabase instance;
    private static String DB_NAME = "icourse.db";

    public abstract RaceCourseDao raceCourseDao();
    public abstract LocationDao locationDao();

    public static ICourseDatabase getInstance(final Context context){
        if(instance != null){
            return instance;
        }else {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(),ICourseDatabase.class, DB_NAME)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context).locationDao().insertAll(RaceLocation.populateData());
                                    getInstance(context).raceCourseDao().insertAll(RaceCourse.populateData());
                                }
                            });
                        }
                    })
                    .build();
            return instance;
        }
    }
}
