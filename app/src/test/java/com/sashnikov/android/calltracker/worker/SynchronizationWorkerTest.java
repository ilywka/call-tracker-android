package com.sashnikov.android.calltracker.worker;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import android.content.Context;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import androidx.work.testing.TestWorkerBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sashnikov.android.calltracker.contentprovider.CallsProvider;
import com.sashnikov.android.calltracker.retrofit.SalesBoosterService;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

/**
 * @author Ilya_Sashnikau
 */
@RunWith(MockitoJUnitRunner.class)
public class SynchronizationWorkerTest {

    @Mock private Context contextMock;
    @Mock private WorkerParameters workerParameters;
    @Mock private CallsProvider callsProviderMock;
    @Mock private SalesBoosterService salesBoosterServiceMock;
    @Mock private SynchronizationSettings preferencesHandlerMock;
    @Mock private FirebaseCrashlytics firebaseCrashlytics;

    private SynchronizationWorker synchronizationWorker;

    @Before
    public void setUp() throws Exception {
        synchronizationWorker = new SynchronizationWorker(
                contextMock, workerParameters, callsProviderMock, salesBoosterServiceMock, preferencesHandlerMock,
                firebaseCrashlytics);

        LocalDateTime now = mock(LocalDateTime.class);
        willReturn(now).given(preferencesHandlerMock).getLastUpdatedDate();
        willReturn(Collections.emptyList()).given(callsProviderMock).callsSince(now);
    }

    @Test
    public void shouldFailWhenServiceUnavailable() throws Exception {
        given(salesBoosterServiceMock.isServiceAvailable()).willReturn(false);

        Result result = synchronizationWorker.doWork();

        then(salesBoosterServiceMock).should(times(1)).isServiceAvailable();
        then(salesBoosterServiceMock).shouldHaveNoMoreInteractions();

        assertThat(result, instanceOf(Result.failure().getClass()));
    }

    @Test
    public void shouldFailWhenSaveFail() throws Exception {
        given(salesBoosterServiceMock.isServiceAvailable()).willReturn(true);
        given(salesBoosterServiceMock.saveCalls(any(List.class))).willReturn(false);

        Result result = synchronizationWorker.doWork();

        then(salesBoosterServiceMock).should(times(1)).isServiceAvailable();
        then(salesBoosterServiceMock).should(times(1)).saveCalls(any(List.class));
        then(salesBoosterServiceMock).shouldHaveNoMoreInteractions();

        assertThat(result, instanceOf(Result.failure().getClass()));
    }

    @Test
    public void shouldSuccess() throws Exception {
        given(salesBoosterServiceMock.isServiceAvailable()).willReturn(true);
        given(salesBoosterServiceMock.saveCalls(any(List.class))).willReturn(true);

        Result result = synchronizationWorker.doWork();

        then(salesBoosterServiceMock).should(times(1)).isServiceAvailable();
        then(salesBoosterServiceMock).should(times(1)).saveCalls(any(List.class));
        then(salesBoosterServiceMock).shouldHaveNoMoreInteractions();

        assertThat(result, instanceOf(Result.success().getClass()));
    }

    private void qwe() {
        SynchronizationWorker worker =
                TestWorkerBuilder.from(
                        contextMock,
                        SynchronizationWorker.class,
                        Executors.newSingleThreadExecutor()
                )
                        .build();

//        Result result = worker.startWork().get();
//        assertThat(result, is(Result.success()));
    }
}