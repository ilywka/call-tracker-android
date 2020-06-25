package com.sashnikov.android.calltracker.worker;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;

import java.util.concurrent.Executors;
import android.content.Context;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import androidx.work.testing.TestWorkerBuilder;
import com.sashnikov.android.calltracker.contentprovider.CallsProvider;
import com.sashnikov.android.calltracker.retrofit.SalesBoosterService;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationPreferencesHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Ilya_Sashnikau
 */
@RunWith(MockitoJUnitRunner.class)
public class SynchronizationWorkerTest {

    @Mock private Context contextMock;
    @Mock private WorkerParameters workerParameters;
    @Mock private CallsProvider callsProviderMock;
    @Mock private SalesBoosterService salesBoosterServiceMock;
    @Mock private SynchronizationPreferencesHandler preferencesHandlerMock;

    private SynchronizationWorker synchronizationWorker;

    @Before
    public void setUp() throws Exception {
        synchronizationWorker = new SynchronizationWorker(
                contextMock, workerParameters, callsProviderMock, salesBoosterServiceMock, preferencesHandlerMock
        );
    }

    @Test
    public void shouldFailWhenServiceUnavailable() throws Exception {
        BDDMockito.given(salesBoosterServiceMock.isServiceAvailable()).willReturn(false);

        Result result = synchronizationWorker.doWork();

        BDDMockito.then(salesBoosterServiceMock).should(times(1)).isServiceAvailable();
        BDDMockito.then(salesBoosterServiceMock).shouldHaveNoMoreInteractions();

        assertThat(result, instanceOf(Result.failure().getClass()));
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