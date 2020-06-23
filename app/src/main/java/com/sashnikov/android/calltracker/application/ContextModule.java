package com.sashnikov.android.calltracker.application;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

/**
 * @author Ilya_Sashnikau
 */
@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationContext
    public Context getContext() {
        return context;
    }

}
