package com.sashnikov.android.calltracker.worker.di;

import android.content.Context;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * @author Ilya_Sashnikau
 */
public interface CustomWorkerFactory {
    Worker create(Context context, WorkerParameters workerParameters);
}
