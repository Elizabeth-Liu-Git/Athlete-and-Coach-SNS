package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * AthletePage class which contains the activity that is accessed by an athlete user
 */

public class AthletePage extends AppCompatActivity {

    private TabLayout tabLayoutAthletePage;
    private ViewPager viewPagerAthletePage;
    private TabItem tabUpcoming, tabHistory;
    public PageAdapter pagerAdapterAthletePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_page);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Instantiating Tab Items
        tabLayoutAthletePage = (TabLayout) findViewById(R.id.athlete_layout);
        tabUpcoming = (TabItem) findViewById(R.id.athlete_tab_upcoming);
        tabHistory = (TabItem) findViewById(R.id.athlete_tab_history);
        viewPagerAthletePage = findViewById(R.id.athlete_viewpager);


        pagerAdapterAthletePage = new PageAdapter(getSupportFragmentManager(), tabLayoutAthletePage.getTabCount());
        viewPagerAthletePage.setAdapter(pagerAdapterAthletePage);

        /**
         * Tab Layout for the Athlete Page
         */
        tabLayoutAthletePage.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * Listener to load information when a tab is selected (position is changed)
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerAthletePage.setCurrentItem(tab.getPosition());
                //First Tab
                if(tab.getPosition() == 0){
                    pagerAdapterAthletePage.notifyDataSetChanged();
                }
                //Second Tab
                else if(tab.getPosition() == 1){
                    pagerAdapterAthletePage.notifyDataSetChanged();
                }

            }

            /**
             * Listener for when tab is unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){

                }
            }

            /**
             * Listener for when tab is reselected
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Adding onpagechangelistener which detects when tab index changes
        viewPagerAthletePage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutAthletePage));

    }

    /**
     * Menu Icons inflated (as they were with actionbar)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.miCompose:
                Intent intent = new Intent(this, ProfilePage.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
