package com.sashnikov.android.calltracker.worker;

import java.util.List;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sashnikov.android.calltracker.contentprovider.CallsProvider;
import com.sashnikov.android.calltracker.model.PhoneCall;
import com.sashnikov.android.calltracker.retrofit.SalesBoosterService;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationPreferencesHandler;
import com.sashnikov.android.calltracker.worker.di.CustomWorkerFactory;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import org.threeten.bp.LocalDateTime;

/**
 * @author Ilya_Sashnikau
 */
public class SynchronizationWorker extends Worker {

    private static final String LOG_TAG = SynchronizationWorker.class.getName();
    private static final String ERROR_MESSAGE_KEY = "synchronizationWorkerErrorMessage";

    private final SynchronizationPreferencesHandler synchronizationPreferencesHandler;
    private final CallsProvider callsProvider;
    private final SalesBoosterService salesBoosterService;

    @AssistedInject
    public SynchronizationWorker(
            @NonNull @Assisted Context context,
            @NonNull @Assisted WorkerParameters workerParameters,
            CallsProvider callsProvider,
            SalesBoosterService salesBoosterService,
            SynchronizationPreferencesHandler synchronizationPreferences
    ) {
        super(context, workerParameters);
        synchronizationPreferencesHandler = synchronizationPreferences;
        this.callsProvider = callsProvider;
        this.salesBoosterService = salesBoosterService;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (!salesBoosterService.isServiceAvailable()) {
                return Result.failure(createOutputDataOnError("Service unavailable"));
            }
            LocalDateTime lastUpdatedDate = synchronizationPreferencesHandler.getLastUpdatedDate();

            String message =
                    String.format(
                            "Starting data synchronization. Last updated date: %s",
                            lastUpdatedDate.toString()
                    );
            Log.i(LOG_TAG, message);

            List<PhoneCall> phoneCalls = callsProvider.callsSince(lastUpdatedDate);

            String msg = String.format("Retrieved phone calls: %s", phoneCalls.toString());
            Log.i(LOG_TAG, msg);

            salesBoosterService.saveCalls(phoneCalls);
            return Result.success();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e(LOG_TAG, "Error during synchronization.", e);
            return Result.failure(createOutputDataOnError(e.getMessage()));
        }
    }

    private Data createOutputDataOnError(String message) {
        return new Data.Builder()
                .putString(ERROR_MESSAGE_KEY, message)
                .build();
    }

    @AssistedInject.Factory
    public interface Factory extends CustomWorkerFactory {
    }

}
