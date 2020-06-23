package com.sashnikov.android.calltracker.worker;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.sashnikov.android.calltracker.db.repository.UserRepository;
import com.sashnikov.android.calltracker.model.User;
import com.sashnikov.android.calltracker.retrofit.TestService;
import com.sashnikov.android.calltracker.util.SettingsUtil;
import com.sashnikov.android.calltracker.worker.di.CustomWorkerFactory;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;

/**
 * @author Ilya_Sashnikau
 */
public class BackgroundWorker extends Worker {

    public static final String AGE_DELTA_KEY = "age_delta";
    public static final String USER_ID_KEY = "user_id";
    public static final String PROGRESS_ACTION_KEY = "current_action";
    private final UserRepository userRepository;
    private final TestService testService;
    private final int delta;
    private final long userId;

    @AssistedInject
    public BackgroundWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters workerParameters,
            UserRepository userRepository,
            TestService testService
    ) {
        super(context, workerParameters);
        this.userRepository = userRepository;
        this.testService = testService;
        delta = workerParameters.getInputData().getInt(AGE_DELTA_KEY, 0);
        userId = workerParameters.getInputData().getLong(USER_ID_KEY, 0L);
    }

    @NonNull
    @Override
    public Result doWork() {
        boolean shouldDoWork = SettingsUtil.getEnabledBackgroundTaskOnAddAgeClick(this.getApplicationContext());
        if (shouldDoWork) {
            Data data = getProgressActionData("Starting");
            setProgressAsync(data);
            sleep(500);

            Log.i("Background", "BACK WORK DONE");

            data = getProgressActionData("Updating user");
            setProgressAsync(data);
            sleep(500);

            User user = userRepository.getUser(userId);
            Integer currAge = user.getAge();
            user.setAge(currAge - this.delta);
            userRepository.update(user);
            data = getProgressActionData("Calling service");
            setProgressAsync(data);
            sleep(1000);

            try {
                callService(currAge);
            } catch (IOException e) {
                Log.i("Retrofit", "Retrofit task failed");
            }
        }
        return Result.success();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Data getProgressActionData(String actionName) {
        return new Data.Builder()
                .putString(PROGRESS_ACTION_KEY, actionName)
                .build();
    }

    private void callService(Integer currAge) throws IOException {
        Map<String, String> map = Collections.singletonMap("age", currAge.toString());
        Map<String, String> mapResult = testService.callService(map);
        Log.i("BackgroundServiceCall", map.toString() + ", result: " + mapResult.toString());
    }

    @AssistedInject.Factory
    public interface Factory extends CustomWorkerFactory {
    }
}
