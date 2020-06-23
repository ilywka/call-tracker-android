package com.sashnikov.android.calltracker.ui.activity.synchronization;

import javax.inject.Inject;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.work.WorkManager;
import com.sashnikov.android.calltracker.R;
import com.sashnikov.android.calltracker.application.CallTrackerApplication;
import com.sashnikov.android.calltracker.databinding.ActivitySynchronizationBinding;
import com.sashnikov.android.calltracker.databinding.ContentSynchronizationBinding;
import com.sashnikov.android.calltracker.viewmodel.SynchronizationViewModel;

public class SynchronizationActivity extends AppCompatActivity {

    public static final String NO_NETWORK_CONNECTION_MESSAGE = "No network connection";
    @Inject
    SynchronizationViewModel synchronizationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySynchronizationBinding binding = setupActivityBinding();

        setUpSynchronization(binding);

        Toolbar toolbar = binding.synchronizationToolbar;
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.synchronization_preferences, false);
    }

    private void setUpSynchronization(ActivitySynchronizationBinding binding) {
        ContentSynchronizationBinding contentBinding = binding.synchronizationContent;
        Button btnSynchronization = contentBinding.btnSynchronization;
        btnSynchronization
                .setOnClickListener(v -> {
                    btnSynchronization.setEnabled(false);
                    synchronizationViewModel.synchronizeData();
                });

        WorkManager workManager = WorkManager.getInstance(this);
        synchronizationViewModel.getSynchronizationWorkId()
                .observe(this, new SynchronizationStatusObserver(this, workManager, contentBinding));

        synchronizationViewModel.getIsNetworkConnected().observe(
                SynchronizationActivity.this,
                isNetworkConnected -> {
                    if (!isNetworkConnected) {
                        showNoNetworkMessage();
                    }
                });
    }

    private ActivitySynchronizationBinding setupActivityBinding() {
        ((CallTrackerApplication) getApplicationContext()).getApplicationComponent().inject(this);
        ActivitySynchronizationBinding binding = ActivitySynchronizationBinding.inflate(getLayoutInflater());
        binding.setSynchronizationViewModel(synchronizationViewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        return binding;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_synchronization, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.synchronization_settings) {

            Intent intent = new Intent(this, SynchronizationSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNoNetworkMessage() {
        Toast.makeText(this, NO_NETWORK_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
    }
}
