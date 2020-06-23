package com.sashnikov.android.calltracker.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sashnikov.android.calltracker.ui.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_SWITCH_BACKGROUND_TASK_ON_BUTTON = "run_task_on_button_click";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
