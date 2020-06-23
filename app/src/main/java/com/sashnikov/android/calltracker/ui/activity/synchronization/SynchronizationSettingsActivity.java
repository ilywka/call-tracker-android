package com.sashnikov.android.calltracker.ui.activity.synchronization;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sashnikov.android.calltracker.ui.fragment.SynchronizationSettingsFragment;

public class SynchronizationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SynchronizationSettingsFragment())
                .commit();
    }
}
