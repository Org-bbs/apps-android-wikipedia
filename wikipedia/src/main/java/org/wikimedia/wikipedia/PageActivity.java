package org.wikimedia.wikipedia;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.*;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class PageActivity extends FragmentActivity {

    Button pageGoButton;
    EditText pageNameText;

    private Bus bus;
    private WikipediaApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((WikipediaApp)getApplicationContext());
        bus = app.getBus();
        bus.register(this);

        pageGoButton = (Button) findViewById(R.id.pageGoButton);
        pageNameText = (EditText) findViewById(R.id.pageNameText);

        pageGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageTitle title = new PageTitle(null, pageNameText.getText().toString(), app.getPrimarySite());
                displayNewPage(title);
            }
        });
    }

    private void displayNewPage(PageTitle title) {
        PageViewFragment pageFragment = new PageViewFragment(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onNewWikiPageNavigationEvent(LinkHandler.NewWikiPageNavigationEvent event) {
        displayNewPage(event.getTitle());
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        PageViewFragment viewFragment = (PageViewFragment) getSupportFragmentManager().findFragmentById(R.id.testFragment);
//        viewFragment.displayPage(new PageTitle(null, "India"));
        return super.onMenuItemSelected(featureId, item);
    }
}
