package com.sashnikov.android.calltracker.retrofit;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import android.util.Log;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sashnikov.android.calltracker.model.PhoneCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author Ilya_Sashnikau
 */
public class SalesBoosterService {
    private static final String LOG_TAG = SalesBoosterService.class.getName();

    private final SalesBoosterApi salesBoosterApi;

    @Inject
    public SalesBoosterService(SalesBoosterApi salesBoosterApi) {
        this.salesBoosterApi = salesBoosterApi;
    }

    public boolean saveCalls(List<PhoneCall> phoneCalls) {
        Call<ResponseBody> call = salesBoosterApi.save(phoneCalls);
        try {
            Response<ResponseBody> response = call.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e(LOG_TAG, "Error saving data", e);
            return false;
        }
    }

    public boolean isServiceAvailable() {
        Call<ResponseBody> healthCall = salesBoosterApi.health();
        try {
            Response<ResponseBody> response = healthCall.execute();
            boolean successful = response.isSuccessful();
            if (!successful) {
                throw new HttpException(response);
            }

            return successful;
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e(LOG_TAG, "service unavailable.", e);
            return false;
        }
    }
}
