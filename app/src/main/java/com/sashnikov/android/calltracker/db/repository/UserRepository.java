package com.sashnikov.android.calltracker.db.repository;

import javax.inject.Inject;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.sashnikov.android.calltracker.application.ApplicationContext;
import com.sashnikov.android.calltracker.db.CallTrackerDatabase;
import com.sashnikov.android.calltracker.model.User;

/**
 * @author Ilya_Sashnikau
 */
public class UserRepository {
    private static UserRepository instance;

    private final CallTrackerDatabase database;

    @Inject
    public UserRepository(
            @ApplicationContext Context applicationContext
    ) {
        this.database = CallTrackerDatabase.getInstance(applicationContext);
    }

    public static UserRepository getInstance(Application application) {
        if (instance==null) {
            synchronized (UserRepository.class) {
                if (instance==null) {
                    instance = new UserRepository(application);
                }
            }
        }
        return instance;
    }

    public LiveData<User> getUserLive(long id) {
//        CallTrackerDatabase.DATABASE_EXECUTOR.execute(() -> {
//            System.out.println(database.userDao().getAllUsers());
//        });
        Log.i("qwe", "getting user live data with id " + id);
        return database.userDao().getLiveById(id);
    }

    public User getUser(long id) {
//        CallTrackerDatabase.DATABASE_EXECUTOR.execute(() -> {
//            System.out.println(database.userDao().getAllUsers());
//        });
        Log.i("qwe", "getting user with id " + id);
        return database.userDao().getById(id);
    }

    public void insert(User user) {
        database.userDao().insertUser(user);
    }

    public void update(User user) {
        database.userDao().updateUser(user);
    }
}
