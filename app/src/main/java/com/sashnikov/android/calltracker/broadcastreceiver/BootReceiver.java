package com.sashnikov.android.calltracker.broadcastreceiver;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.sashnikov.android.calltracker.worker.BackgroundWorker;

/**
 * @author Ilya_Sashnikau
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_BOOT_COMPLETED)) {
        }

        if (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON)) {
            OneTimeWorkRequest oneTimeWorkRequest =
                    new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                            .build();
            WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
        }
    }

}
