package com.sashnikov.android.calltracker.application;

import javax.inject.Singleton;
import com.sashnikov.android.calltracker.retrofit.di.NetworkModule;
import com.sashnikov.android.calltracker.ui.activity.MainActivity;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationActivity;
import com.sashnikov.android.calltracker.worker.WorkerAbstractFactory;
import com.sashnikov.android.calltracker.worker.di.WorkerModule;
import dagger.Component;

/**
 * @author Ilya_Sashnikau
 */
@Singleton
@Component(modules = {ContextModule.class, WorkerModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(SynchronizationActivity mainActivity);

    WorkerAbstractFactory workerFactory();

}
