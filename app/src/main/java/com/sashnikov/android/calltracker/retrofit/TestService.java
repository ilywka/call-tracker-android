package com.sashnikov.android.calltracker.retrofit;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Ilya_Sashnikau
 */
public class TestService {

    private final TestRetrofitService testRetrofitService;

    @Inject
    public TestService(TestRetrofitService testRetrofitService) {
        this.testRetrofitService = testRetrofitService;
    }

    public Map<String, String> callService(Map<String, String> query) throws IOException {
        Call<TestServiceResponse> responseCall = testRetrofitService.testGetMethod(query);
        Response<TestServiceResponse> response = responseCall.execute();
        return response.body().getArgs();
    }
}
