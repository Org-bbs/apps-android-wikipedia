package org.wikimedia.wikipedia.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import org.mediawiki.api.json.Api;
import org.wikimedia.wikipedia.Page;
import org.wikimedia.wikipedia.PageFetchTask;
import org.wikimedia.wikipedia.PageTitle;
import org.wikimedia.wikipedia.Site;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PageFetchTaskTests extends ActivityUnitTestCase<TestDummyActivity> {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;

    private Api enwikiAPI;
    private Site enwiki;

    public PageFetchTaskTests() {
        super(TestDummyActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        enwiki = new Site("en.wikipedia.org");
        enwikiAPI = new Api("en.wikipedia.org");

        startActivity(new Intent(), null, null);
    }

    public void testPageFetch() throws Throwable {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                new PageFetchTask(enwikiAPI, new PageTitle(null, "India", enwiki)) {
                    @Override
                    public void onFinish(Page result) {
                        assertNotNull(result);
                        // FIXME: SUPER FLAKY TEST BELOW! 15 is count of first level sections + 1 in en:India article!
                        assertEquals(15, result.getSections().size());
                        assertEquals(4, result.getSections().get(2).getSubSections().size());
                        completionLatch.countDown();
                    }
                }.execute();
            }
        });
        assertTrue(completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS));
    }
}
