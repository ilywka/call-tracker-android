package com.sashnikov.android.calltracker.ui.activity.synchronization;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import androidx.core.util.Consumer;
import androidx.preference.PreferenceManager;
import com.sashnikov.android.calltracker.R;
import com.sashnikov.android.calltracker.application.ApplicationContext;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * @author Ilya_Sashnikau
 */
public class SynchronizationSettings {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final String PREFERENCES_FILE_NAME =
            "com.sashnikov.andorid.calltracker.synhronizationprefs";
    private static final String LAST_UPDATED_DATE_KEY = "lastUpdatedDate";

    private final Context context;
    private final List<OnSharedPreferenceChangeListener> listeners;

    @Inject
    public SynchronizationSettings(
           @ApplicationContext Context context
    ) {
        this.context = context;
        this.listeners = new LinkedList<>();
    }

    private SharedPreferences getPreferences() {
        return this.context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public LocalDateTime getLastUpdatedDate() {
        String dateTimeString = getPreferences().getString(LAST_UPDATED_DATE_KEY, "");
        if ("".equals(dateTimeString)) {
            return LocalDateTime.now(ZoneId.systemDefault()).minusDays(2);
        }
        return toDateTime(dateTimeString);
    }

    public String getLastUpdatedDateString() {
        return getPreferences().getString(LAST_UPDATED_DATE_KEY, "");
    }

    public void setLastUpdatedDate(LocalDateTime localDateTime) {
        Editor editor = getPreferences().edit();
        editor.putString(LAST_UPDATED_DATE_KEY, toString(localDateTime));
        editor.apply();
    }

    public void observeLastUpdatedDate(Consumer<LocalDateTime> action) {
        OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
            if (LAST_UPDATED_DATE_KEY.equals(key)) {
                String localDateTime = sharedPreferences.getString(key, null);
                action.accept(toDateTime(localDateTime));
            }
        };
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
        this.listeners.add(listener);
    }

    public void observeLastUpdatedDateString(Consumer<String> action) {
        OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
            if (LAST_UPDATED_DATE_KEY.equals(key)) {
                String localDateTime = sharedPreferences.getString(key, null);
                action.accept(localDateTime);
            }
        };
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
        this.listeners.add(listener);
    }

    public String getServiceUrl() {
        String backendUrlSettingKey = context.getResources().getString(R.string.backend_url_setting_key);
        return getDefaultSharedPreferences().getString(backendUrlSettingKey, "");
    }

    private SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static String toString(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    private static LocalDateTime toDateTime(String localDateTimeString) {
        return LocalDateTime.parse(localDateTimeString, DATE_TIME_FORMATTER);
    }

}
