package com.sashnikov.android.calltracker.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.sashnikov.android.calltracker.db.dao.UserDao;
import com.sashnikov.android.calltracker.model.User;

/**
 * @author Ilya_Sashnikau
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class CallTrackerDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "call_tracker_database";

    @VisibleForTesting
    public static final ExecutorService DATABASE_EXECUTOR = Executors.newFixedThreadPool(2);

    private static CallTrackerDatabase instance;

    public static CallTrackerDatabase getInstance(Context context) {
        if (instance==null) {
            synchronized (CallTrackerDatabase.class) {
                if (instance==null) {
                    instance = createDb(context);
                }
            }
        }
        return instance;
    }

    private static CallTrackerDatabase createDb(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                CallTrackerDatabase.class,
                DATABASE_NAME)
                .addCallback(populateDbCallback(context))
                .allowMainThreadQueries()
                .build();
    }

    private static Callback populateDbCallback(Context context) {
        return new Callback() {

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                DATABASE_EXECUTOR.submit(() -> {
                    getInstance(context).userDao().insertUser(new User(123L, "Vasy", 21));
                });
            }
        };
    }


    public abstract UserDao userDao();
}
