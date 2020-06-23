package com.sashnikov.android.calltracker.ui.activity.synchronization;

import java.util.UUID;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.WorkInfo;
import androidx.work.WorkInfo.State;
import androidx.work.WorkManager;
import com.sashnikov.android.calltracker.databinding.ContentSynchronizationBinding;

/**
 * @author Ilya_Sashnikau
 */
public class SynchronizationStatusObserver implements Observer<UUID> {

    private final WorkManager workManager;
    private final ContentSynchronizationBinding contentBinding;
    private final SynchronizationActivity synchronizationActivity;

    public SynchronizationStatusObserver(
            SynchronizationActivity synchronizationActivity,
            WorkManager workManager,
            ContentSynchronizationBinding contentBinding) {
        this.synchronizationActivity = synchronizationActivity;
        this.workManager = workManager;
        this.contentBinding = contentBinding;
    }

    @Override
    public void onChanged(UUID uuid) {
        if (uuid == null) {
            updateTextStatus("");
        } else {
            LiveData<WorkInfo> workInfoLiveData = workManager.getWorkInfoByIdLiveData(uuid);
            setUpStatusObserving(workInfoLiveData);
        }
    }

    private void updateTextStatus(String status) {
        contentBinding.synchronizationStatusText.setText(status);
    }

    private void setUpStatusObserving(LiveData<WorkInfo> workInfoByIdLiveData) {
        workInfoByIdLiveData.observe(synchronizationActivity, this::onWorkInfoChange);
    }

    private void onWorkInfoChange(WorkInfo workInfo) {
        State state = workInfo.getState();
        if (state.isFinished()) {
            updateTextStatus(String.format("Done (%s)", state.name()));
            contentBinding.btnSynchronization.setEnabled(true);
        } else {
            updateTextStatus(state.name());
        }
    }
}
