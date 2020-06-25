package com.sashnikov.android.calltracker.contentprovider;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.util.Log;
import com.sashnikov.android.calltracker.application.ApplicationContext;
import com.sashnikov.android.calltracker.model.PhoneCall;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

/**
 * @author Ilya_Sashnikau
 */
public class CallsProvider {

    private static final String LOG_TAG = CallsProvider.class.getName();
    private final Context applicationContext;
    private final ContentResolver contentResolver;

    @Inject
    public CallsProvider(
            @ApplicationContext Context applicationContext
    ) {
        this.applicationContext = applicationContext;
        contentResolver = applicationContext.getContentResolver();
    }

    public List<PhoneCall> callsSince(LocalDateTime sinceDateTime) {

        List<PhoneCall> phoneCalls = new ArrayList<>();
        String[] projection = {
                Calls.NUMBER,
                Calls.DATE,
                Calls.TYPE,
                Calls.DURATION
        };

        try (Cursor cursor = createCursor(projection, sinceDateTime)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int idx = 0;
                    String number = cursor.getString(cursor.getColumnIndex(projection[idx++]));

                    long dateLong = cursor.getLong(cursor.getColumnIndex(projection[idx++]));
                    LocalDateTime callDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());

                    int typeInt = cursor.getInt(cursor.getColumnIndex(projection[idx++]));
                    CallType callType = CallType.get(typeInt);
                    long durationSeconds = cursor.getLong(cursor.getColumnIndex(projection[idx++]));

                    PhoneCall phoneCall = new PhoneCall(
                            number,
                            callDate,
                            callType,
                            durationSeconds
                    );
                    phoneCalls.add(phoneCall);
                }
            }
        }
        return phoneCalls;
    }

    private Cursor createCursor(String[] projection, LocalDateTime sinceDateTime) {
        String selectionClause = String.format("%s > ?", Calls.DATE);

        long sinceEpochMills = 1000 * sinceDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();

        String[] selectionArgs = {Long.toString(sinceEpochMills)};
        String sortOrder = null;
        return contentResolver.query(
                Calls.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);
    }

    public enum CallType {
        INCOMING_TYPE(1),
        OUTGOING_TYPE(2),
        MISSED_TYPE(3),
        VOICEMAIL_TYPE(4),
        REJECTED_TYPE(5),
        BLOCKED_TYPE(6),
        ANSWERED_EXTERNALLY_TYPE(7);

        private final int value;

        CallType(int value) {
            this.value = value;
        }

        static CallType get(int value) {
            for (CallType callType : CallType.values()) {
                if (callType.value == value) {
                    return callType;
                }
            }
            throw new RuntimeException("No call type with value " + value);
        }
    }
}
