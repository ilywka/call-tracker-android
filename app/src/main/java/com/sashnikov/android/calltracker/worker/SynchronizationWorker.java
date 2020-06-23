package com.sashnikov.android.calltracker.worker;

import java.util.List;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.sashnikov.android.calltracker.model.PhoneCall;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynhronizationPreferencesHandler;
import com.sashnikov.android.calltracker.worker.di.CustomWorkerFactory;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import org.threeten.bp.LocalDateTime;

/**
 * @author Ilya_Sashnikau
 */
public class SynchronizationWorker extends Worker {

    private final SynhronizationPreferencesHandler synhronizationPreferencesHandler;

    @AssistedInject
    public SynchronizationWorker(
            @NonNull @Assisted Context context,
            @NonNull @Assisted WorkerParameters workerParameters) {
        super(context, workerParameters);
        synhronizationPreferencesHandler = new SynhronizationPreferencesHandler(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        LocalDateTime lastUpdatedDate = synhronizationPreferencesHandler.getLastUpdatedDate();
        List<PhoneCall> phoneCalls = getLogsSince(lastUpdatedDate);
        save(phoneCalls);
        return Result.success();
    }

    private void save(List<PhoneCall> phoneCalls) {
    }

    private List<PhoneCall> getLogsSince(LocalDateTime localDateTime) {
        return null;
    }

    @AssistedInject.Factory
    public interface Factory extends CustomWorkerFactory {
    }

}
