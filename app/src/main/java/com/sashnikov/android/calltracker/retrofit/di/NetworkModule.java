package com.sashnikov.android.calltracker.retrofit.di;

import javax.inject.Singleton;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sashnikov.android.calltracker.retrofit.SalesBoosterApi;
import com.sashnikov.android.calltracker.retrofit.TestRetrofitService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Ilya_Sashnikau
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public TestRetrofitService provideTestRetrofitService(
            OkHttpClient okHttpClient,
            Converter.Factory converterFactory
    ) {
        return new Retrofit.Builder()
                .baseUrl("https://postman-echo.com")
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
                .create(TestRetrofitService.class);
    }

    @Provides
    @Singleton
    public SalesBoosterApi provideSalesBoosterApi(
            OkHttpClient okHttpClient,
            Factory converterFactory
    ) {
        return new Retrofit.Builder()
                .baseUrl("https://postman-echo.com")
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
                .create(SalesBoosterApi.class);
    }


    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    public Converter.Factory jacksonConverterFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return JacksonConverterFactory.create(objectMapper);
    }
}
