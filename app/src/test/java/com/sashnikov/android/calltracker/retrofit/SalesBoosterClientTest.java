package com.sashnikov.android.calltracker.retrofit;

import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;

import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationSettings;
import org.junit.Test;

/**
 * @author Ilya_Sashnikau
 */
public class SalesBoosterClientTest {

    @Test
    public void shouldBuildCorrectUrlWhenBaseUrlWithLastSlash() {
        SynchronizationSettings preferencesHandler =
                mock(SynchronizationSettings.class);
        SalesBoosterRetrofitApi api = mock(SalesBoosterRetrofitApi.class);

        willReturn("path/").given(preferencesHandler).getServiceUrl();

        SalesBoosterClient client = new SalesBoosterClient(preferencesHandler, api);
        client.health();

        then(api).should(times(1)).health("path/api/health");
    }

    @Test
    public void shouldBuildCorrectUrlWhenBaseUrlWithoutLastSlash() {
        SynchronizationSettings preferencesHandler =
                mock(SynchronizationSettings.class);
        SalesBoosterRetrofitApi api = mock(SalesBoosterRetrofitApi.class);

        willReturn("path").given(preferencesHandler).getServiceUrl();

        SalesBoosterClient client = new SalesBoosterClient(preferencesHandler, api);
        client.health();

        then(api).should(times(1)).health("path/api/health");
    }
}