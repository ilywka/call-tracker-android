package com.sashnikov.android.calltracker.retrofit;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author Ilya_Sashnikau
 */
public interface TestRetrofitService {

    @GET("/get")
    Call<TestServiceResponse> testGetMethod(@QueryMap Map<String, String> queryParameters);
}
