package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class AthletePage extends AppCompatActivity {

    /**
     *
     */
    Button signOut;
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
        viewPagerAthletePage = findViewById(R.id.athlete_viewpager);

        pagerAdapterAthletePage = new PageAdapter(getSupportFragmentManager(), tabLayoutAthletePage.getTabCount());
        viewPagerAthletePage.setAdapter(pagerAdapterAthletePage);


        /**
         *
         */
        tabLayoutAthletePage.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerAthletePage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutAthletePage));

    }
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignIn.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}