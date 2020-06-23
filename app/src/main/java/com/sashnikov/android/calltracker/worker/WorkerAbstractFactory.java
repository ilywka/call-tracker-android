package com.sashnikov.android.calltracker.worker;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;
import com.sashnikov.android.calltracker.worker.di.CustomWorkerFactory;

/**
 * @author Ilya_Sashnikau
 */
public class WorkerAbstractFactory extends WorkerFactory {

    private final Map<String, Provider<CustomWorkerFactory>> nameFactoryMap;

    @Inject
    public WorkerAbstractFactory(Map<Class<? extends Worker>, Provider<CustomWorkerFactory>> classFactoryMap) {
        this.nameFactoryMap = new HashMap<>(classFactoryMap.size(), 1);
        for (Map.Entry<Class<? extends Worker>, Provider<CustomWorkerFactory>> classProviderEntry : classFactoryMap.entrySet()) {
            nameFactoryMap.put(classProviderEntry.getKey().getName(), classProviderEntry.getValue());
        }
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(
            @NonNull Context appContext,
            @NonNull String workerClassName,
            @NonNull WorkerParameters workerParameters
    ) {
        return nameFactoryMap.get(workerClassName).get().create(appContext, workerParameters);
    }
}
