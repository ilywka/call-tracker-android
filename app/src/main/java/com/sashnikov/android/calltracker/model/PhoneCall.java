package com.sashnikov.android.calltracker.model;

import com.sashnikov.android.calltracker.contentprovider.CallsProvider.CallType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.threeten.bp.LocalDateTime;

/**
 * @author Ilya_Sashnikau
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PhoneCall {

    private final String number;
    private final LocalDateTime date;
    private final CallType callType;
    private final long durationSeconds;

}
