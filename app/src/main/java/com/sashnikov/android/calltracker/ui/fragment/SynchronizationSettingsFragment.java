package com.sashnikov.android.calltracker.ui.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import com.sashnikov.android.calltracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SynchronizationSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.synchronization_preferences, rootKey);
    }
}
