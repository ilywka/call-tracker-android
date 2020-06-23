package com.sashnikov.android.calltracker.worker.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import androidx.work.Worker;
import dagger.MapKey;

/**
 * @author Ilya_Sashnikau
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface WorkerKey {
    Class<? extends Worker> value();
}
