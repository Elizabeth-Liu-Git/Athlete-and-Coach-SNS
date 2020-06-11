package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.activitymonitor.model.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Update UserType parameter to 2
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(fUser.getUid()).update("userType",2);
        
        //Instantiating Tab Items
        tabLayoutAthletePage = (TabLayout) findViewById(R.id.athlete_layout);
        tabUpcoming = (TabItem) findViewById(R.id.athlete_tab_upcoming);
        tabHistory = (TabItem) findViewById(R.id.athlete_tab_history);
        viewPagerAthletePage = findViewById(R.id.athlete_viewpager);

        pagerAdapterAthletePage = new PageAdapter(getSupportFragmentManager(), tabLayoutAthletePage.getTabCount());
        viewPagerAthletePage.setAdapter(pagerAdapterAthletePage);

        tabLayoutAthletePage.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerAthletePage.setCurrentItem(tab.getPosition());
                //First Tab
                if(tab.getPosition() == 0){

                }
                //Second Tab
                else if(tab.getPosition() == 1){
                    pagerAdapterAthletePage.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0){

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerAthletePage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutAthletePage));

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
