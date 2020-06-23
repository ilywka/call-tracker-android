package com.sashnikov.android.calltracker.worker.di;

import com.sashnikov.android.calltracker.worker.BackgroundWorker;
import com.sashnikov.android.calltracker.worker.SynchronizationWorker;
import com.squareup.inject.assisted.dagger2.AssistedModule;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author Ilya_Sashnikau
 */
@AssistedModule
@Module(includes = AssistedInject_WorkerModule.class)
public interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(BackgroundWorker.class)
    CustomWorkerFactory bindBackgroundWorker(BackgroundWorker.Factory factory);

    @Binds
    @IntoMap
    @WorkerKey(SynchronizationWorker.class)
    CustomWorkerFactory bindSynchronizationWorker(SynchronizationWorker.Factory factory);
}
