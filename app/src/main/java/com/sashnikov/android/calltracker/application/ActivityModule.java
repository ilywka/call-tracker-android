package com.sashnikov.android.calltracker.application;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

/**
 * @author Ilya_Sashnikau
 */
@Module
public class ActivityModule {

    private final Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context getContext() {
        return context;
    }
}
