package com.sashnikov.android.calltracker.retrofit;

import java.util.List;
import com.sashnikov.android.calltracker.model.PhoneCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author Ilya_Sashnikau
 */
public interface SalesBoosterRetrofitApi {

    @POST
    Call<ResponseBody> save(@Url String url, List<PhoneCall> phoneCalls);

    @GET
    Call<ResponseBody> health(@Url String url);
}
