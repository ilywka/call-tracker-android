package com.sashnikov.android.calltracker.retrofit;

import javax.inject.Inject;
import java.util.List;
import com.sashnikov.android.calltracker.model.PhoneCall;
import com.sashnikov.android.calltracker.ui.activity.synchronization.SynchronizationSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author Ilya_Sashnikau
 */
public class SalesBoosterClient {

    private static final String HEALTH_PATH = "/api/health";
    private static final String CALLS_PATH = "/api/calls";

    private final SalesBoosterRetrofitApi api;
    private final SynchronizationSettings preferencesHandler;

    @Inject
    public SalesBoosterClient(
            SynchronizationSettings preferencesHandler,
            SalesBoosterRetrofitApi api
    ) {
        this.preferencesHandler = preferencesHandler;
        this.api = api;
    }

    public Call<ResponseBody> save(List<PhoneCall> phoneCalls) {
        return api.save(getUrl(CALLS_PATH), phoneCalls);
    }

    public Call<ResponseBody> health() {
        return api.health(getUrl(HEALTH_PATH));
    }

    private String getUrl(String path) {
        String serviceUrl = preferencesHandler.getServiceUrl();

        StringBuilder urlBuilder = new StringBuilder(serviceUrl);
        int serviceUrlLength = urlBuilder.length();
        boolean isEndsWithSlash = urlBuilder.charAt(serviceUrlLength - 1) == '/';
        if (isEndsWithSlash) {
            urlBuilder.deleteCharAt(serviceUrlLength - 1);
        }
        return urlBuilder.append(path).toString();
    }
}
