package com.sashnikov.android.calltracker.retrofit.di;

import static org.mockito.BDDMockito.mock;

import com.sashnikov.android.calltracker.retrofit.SalesBoosterRetrofitApi;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Converter.Factory;

/**
 * @author Ilya_Sashnikau
 */
public class NetworkModuleTest {

    @Test
    public void provideTestRetrofitService() {
        OkHttpClient clientMock = mock(OkHttpClient.class);
        Factory factoryMock = mock(Factory.class);
        SalesBoosterRetrofitApi testRetrofitService =
                new NetworkModule().provideSalesBoosterApi(clientMock, factoryMock);

        Assert.assertNotNull(testRetrofitService);
    }
}