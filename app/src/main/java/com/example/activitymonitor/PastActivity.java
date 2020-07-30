package com.example.activitymonitor;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Page that shows the historical view of the athlete's work out
 */
public class PastActivity extends CoachCalendar {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_activity);


        //In this case im setting creator as athlete
        ArrayList<HistoricalActivity> activityArrayList = new ArrayList<>();

        for (int i = 0; i < exerciseNameArray.size(); i++) {
            activityArrayList.add(new HistoricalActivity(exerciseNameArray.get(i), athleteNameArray.get(i),
                    noteArray.get(i), "1", "1", false, 25200, dateArray.get(i)));
        }

        //creates/calls the recycle view and passes the arrayList
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdaptor = new CoachAdaptor(activityArrayList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);
        exerciseNameArray.clear();
        athleteNameArray.clear();
        noteArray.clear();
        dateArray.clear();
    }
}
