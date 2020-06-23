package com.sashnikov.android.calltracker.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Application;
import androidx.work.Configuration;
import androidx.work.WorkManager;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * @author Ilya_Sashnikau
 */
public class CallTrackerApplication extends Application {

    public static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(3);

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        component = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

        Configuration workManagerConfiguration = new Configuration.Builder()
                .setWorkerFactory(component.workerFactory())
                .build();
        WorkManager.initialize(this, workManagerConfiguration);
    }

    public ApplicationComponent getApplicationComponent() {
        return component;
    }
}
