package com.akat.filmreel.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;

/**
 * Helper method for testing LiveData objects, from
 * https://github.com/googlesamples/android-architecture-components.
 *
 * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
 * Once we got a notification via onChanged, we stop observing.
 */
@SuppressWarnings("unchecked")
public class LiveDataTestUtil {
    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        Object[] data = new Object[]{1};
        CountDownLatch latch = new CountDownLatch(1);
        liveData.observeForever(t -> {
            data[0] = t;
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }
}