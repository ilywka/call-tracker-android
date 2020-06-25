package com.sashnikov.android.calltracker.retrofit;

import java.util.List;
import com.sashnikov.android.calltracker.model.PhoneCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Ilya_Sashnikau
 */
public interface SalesBoosterApi {

    @POST("/api/calls/")
    Call<ResponseBody> save(List<PhoneCall> phoneCalls);

    @GET("/api/health")
    Call<ResponseBody> health();
}
