package com.sashnikov.android.calltracker.viewmodel;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.sashnikov.android.calltracker.application.ApplicationContext;
import com.sashnikov.android.calltracker.db.repository.UserRepository;
import com.sashnikov.android.calltracker.model.User;
import com.sashnikov.android.calltracker.worker.BackgroundWorker;

/**
 * @author Ilya_Sashnikau
 */
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<Long> userId;
    private final LiveData<User> user;
    private final Context context;


    @Inject
    public UserViewModel(
            @ApplicationContext Context context,
            UserRepository userRepository
    ) {
        this.context = context;
        this.userRepository = userRepository;
        this.userId = new MutableLiveData<>(123L);
        this.user = Transformations.switchMap(this.userId, userRepository::getUserLive);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.userRepository.insert(user);
    }

    public void updateUser(User user) {
        this.userRepository.update(user);
    }

    public MutableLiveData<Long> getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId.setValue(userId);
    }

    public LiveData<WorkInfo> addAge() {
        int delta = 1;
        User user = this.user.getValue();
        user.setAge(user.getAge() + delta);
        userRepository.update(user);
        Data inputData = new Data.Builder()
                .putInt(BackgroundWorker.AGE_DELTA_KEY, delta)
                .putLong(BackgroundWorker.USER_ID_KEY, user.getId())
                .build();
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                        .setInputData(inputData)
                        .setInitialDelay(100, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager workManager = WorkManager.getInstance(context);

        LiveData<WorkInfo> infoLiveData = workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.getId());
        workManager.enqueue(oneTimeWorkRequest);
        return infoLiveData;
    }

}
