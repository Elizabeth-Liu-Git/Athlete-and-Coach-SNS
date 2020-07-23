package com.example.activitymonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.ParseException;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * AthletePage class which contains the activity that is accessed by an athlete user
 */

public class AthletePage extends AppCompatActivity {

    private TabLayout tabLayoutAthletePage;
    private ViewPager viewPagerAthletePage;
    private TabItem tabUpcoming, tabHistory;
    public PageAdapter pagerAdapterAthletePage;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private databaseInteraction dataB = new databaseInteraction(db);

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

        //TODO
        try {
            createAlarm(System.currentTimeMillis()+10, 69, this);
            checkAlarms();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    /**
     * @param t
     * @param req
     * @param c
     * @throws ParseException
     */
    public  void createAlarm(long t, int req, Context c) throws ParseException {
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(c, RemindBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, req, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,t, pendingIntent);
    }

    /**
     * @param t
     * @param req
     * @param c
     * @throws ParseException
     */
    public  void createAlarm(String t, int req, Context c) throws ParseException {

        createAlarm( RemindBroadcast.stringTimeToLong(t), req, c);
    }


    /**
     * @param req Integer value to specify requestCode value
     * @param c Context variable to pass through current context of application
     */
    public void cancelAlarm(int req, Context c){
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(c, RemindBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, req, intent, 0);

        alarmManager.cancel(pendingIntent);

    }

    public void checkAlarms(){
        dataB.readRelevantAssignedActivities(new AsynchCallback() {
            @Override
            public void onCallback(ArrayList<Object> resultList) {
                Log.d("activitiesretreived", resultList.toString());
            }
        });


    }


}
