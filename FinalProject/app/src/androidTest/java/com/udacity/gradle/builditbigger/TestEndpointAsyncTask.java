package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertTrue;

/**
 * Created by VJ on 16/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestEndpointAsyncTask {
    private static final String                         LOG_TAG       = TestEndpointAsyncTask.class.getSimpleName();
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private static String joke;

    @Test
    public void testTask() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new EndpointsAsyncTask(new EndpointsAsyncTask.ResultListener() {
            @Override
            public void onResults(String s) {
                Log.i(LOG_TAG, "joke received: " + s);
                joke = s;
                latch.countDown();
            }
        }).execute();

        latch.await();
        assertTrue(joke != null && !joke.isEmpty());
    }
}
