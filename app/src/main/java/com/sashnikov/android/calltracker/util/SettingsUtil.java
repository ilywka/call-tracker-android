package com.sashnikov.android.calltracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.sashnikov.android.calltracker.ui.activity.SettingsActivity;

/**
 * @author Ilya_Sashnikau
 */
public class SettingsUtil {

    public static Boolean getEnabledBackgroundTaskOnAddAgeClick(Context context) {
        return getBooleanPreference(context, SettingsActivity.KEY_PREF_SWITCH_BACKGROUND_TASK_ON_BUTTON);
    }

    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }
}
