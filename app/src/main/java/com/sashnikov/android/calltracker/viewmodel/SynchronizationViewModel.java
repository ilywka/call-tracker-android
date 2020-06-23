package com.sashnikov.android.calltracker.viewmodel;

import javax.inject.Inject;
import java.util.UUID;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OneTimeWorkRequest.Builder;
import androidx.work.WorkInfo.State;
import androidx.work.WorkManager;
import com.sashnikov.android.calltracker.application.ApplicationContext;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynhronizationPreferencesHandler;
import com.sashnikov.android.calltracker.worker.SynchronizationWorker;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * @author Ilya_Sashnikau
 */
public class SynchronizationViewModel extends ViewModel {

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("MMM-dd HH:mm");
    private final Context context;
    private final WorkManager workManager;
    private final MutableLiveData<Boolean> isNetworkConnected;
    private final MutableLiveData<UUID> synchronizationWorkId;
    private final MutableLiveData<OneTimeWorkRequest> synchronizationWorkRequest;
    private final SynhronizationPreferencesHandler synhronizationPreferencesHandler;
    private final MutableLiveData<String> lastUpdatedLiveData;

    @Inject
    public SynchronizationViewModel(
            @ApplicationContext Context context
    ) {
        this.context = context;
        this.workManager = WorkManager.getInstance(context);
        this.isNetworkConnected = new MutableLiveData<>(isNetworkConnected());
        this.synchronizationWorkId = new MutableLiveData<>();
        this.synchronizationWorkRequest = new MutableLiveData<>();
        this.synhronizationPreferencesHandler = new SynhronizationPreferencesHandler(context);
        LocalDateTime lastUpdatedDate = synhronizationPreferencesHandler.getLastUpdatedDate();
        this.lastUpdatedLiveData = new MutableLiveData<>(formatDate(lastUpdatedDate));
        synhronizationPreferencesHandler.observeLastUpdatedDate(localDateTime -> lastUpdatedLiveData.postValue(formatDate(localDateTime)));
    }

    private String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DATE_TIME_FORMATTER);
    }


    public void synchronizeData() {
        LocalDateTime syncStart = LocalDateTime.now();
        boolean isNetworkConnected = isNetworkConnected();
        this.isNetworkConnected.setValue(isNetworkConnected);

        if (isNetworkConnected) {
            OneTimeWorkRequest synchronizationWorkRequest = createWorkRequest();
            UUID requestId = synchronizationWorkRequest.getId();
            this.synchronizationWorkId.setValue(requestId);
            this.synchronizationWorkRequest.setValue(synchronizationWorkRequest);

            workManager.enqueue(synchronizationWorkRequest);

            //TODO: move to workerclass
            workManager.getWorkInfoByIdLiveData(requestId)
                    .observeForever(workInfo -> {
                        State state = workInfo.getState();
                        if (State.SUCCEEDED.equals(state)) {
                            synhronizationPreferencesHandler.setLastUpdatedDate(syncStart);
                        }
                    });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private OneTimeWorkRequest createWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        Builder builder = new Builder(SynchronizationWorker.class)
                .setConstraints(constraints);
        return builder.build();
    }

    public LiveData<Boolean> getIsNetworkConnected() {
        return isNetworkConnected;
    }

    public LiveData<String> getLastUpdatedLiveData() {
        return lastUpdatedLiveData;
    }

    public MutableLiveData<UUID> getSynchronizationWorkId() {
        return synchronizationWorkId;
    }

    public MutableLiveData<OneTimeWorkRequest> getSynchronizationWorkRequest() {
        return synchronizationWorkRequest;
    }
}
