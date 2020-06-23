package com.sashnikov.android.calltracker.ui.activity;

import javax.inject.Inject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.WorkInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sashnikov.android.calltracker.R;
import com.sashnikov.android.calltracker.application.CallTrackerApplication;
import com.sashnikov.android.calltracker.databinding.ActivityMainBinding;
import com.sashnikov.android.calltracker.databinding.ContentMainBinding;
import com.sashnikov.android.calltracker.model.User;
import com.sashnikov.android.calltracker.viewmodel.UserViewModel;
import com.sashnikov.android.calltracker.worker.BackgroundWorker;

public class MainActivity extends AppCompatActivity {

    @Inject
    UserViewModel userViewModel;
    private Snackbar userNotFoundSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        ((CallTrackerApplication) getApplicationContext()).getApplicationComponent().inject(this);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        activityMainBinding.setUserViewModel(userViewModel);
        activityMainBinding.setLifecycleOwner(this);
        setContentView(activityMainBinding.getRoot());

        setUpButtonActions(activityMainBinding);

        userNotFoundSnackbar = Snackbar.make(
                activityMainBinding.getRoot(),
                "User with given id not found",
                BaseTransientBottomBar.LENGTH_SHORT);


        if (userViewModel.getUser().getValue()==null) {
            userNotFoundSnackbar.show();
        }

        userViewModel.getUser().observe(this, user -> {
            if (user==null) {
                userNotFoundSnackbar.show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    private void setUpButtonActions(ActivityMainBinding activityMainBinding) {
        ContentMainBinding contentMainBinding = activityMainBinding.included;
        Button getByIdButton = contentMainBinding.getByIdButton;
        getByIdButton.setOnClickListener(view -> {
            userViewModel.setUserId(Long.valueOf(contentMainBinding.idValue.getText().toString()));
        });


        Button btnRandomAge = contentMainBinding.btnRandomAge;
        LiveData<User> userLiveData = userViewModel.getUser();
        userLiveData.observe(this, user -> btnRandomAge.setEnabled(user!=null));

        btnRandomAge.setOnClickListener(view -> {
            LiveData<WorkInfo> workInfoLiveData = userViewModel.addAge();
            workInfoLiveData.observe(
                    MainActivity.this,
                    workInfo -> {
                        Data progress = workInfo.getProgress();
                        contentMainBinding.progressText.setText(progress.getString(BackgroundWorker.PROGRESS_ACTION_KEY));
                        boolean isTaskFinished = workInfo.getState().isFinished();
                        if (isTaskFinished) {
                            btnRandomAge.setEnabled(true);
                            getByIdButton.setEnabled(true);
                        } else {
                            btnRandomAge.setEnabled(false);
                            getByIdButton.setEnabled(false);
                        }
                    });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.main_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.i("Main activity", "destroyed");
        super.onDestroy();
    }
}
