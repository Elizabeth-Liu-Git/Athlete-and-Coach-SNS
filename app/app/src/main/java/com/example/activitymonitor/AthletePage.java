package com.example.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AthletePage extends AppCompatActivity {

    /**
     *
     */
    private TabLayout tabLayoutAthletePage;
    private ViewPager viewPagerAthletePage;
    private TabItem tabUpcoming, tabHistory;

    public PageAdapter pagerAdapterAthletePage;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_page);

        //Instantiating Tab Items
        tabLayoutAthletePage = (TabLayout) findViewById(R.id.athlete_layout);
        tabUpcoming = (TabItem) findViewById(R.id.athlete_tab_upcoming);
        tabHistory = (TabItem) findViewById(R.id.athlete_tab_history);

    }





}