package com.example.activitymonitor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.api.Distribution;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tab_0_upcoming extends Fragment {

    //Private Initializations
    private View upcomingView;
    private RecyclerView myActivityList;
    private String currentUserID;

    private FirebaseFirestore db;
    Query query_activities;

    public tab_0_upcoming() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        //Query for Activities
        query_activities = db.collection("Activities");

        currentUserID= "h3dm13R32eJSxWD7aqhh";
        // Inflate the layout for this fragment
        upcomingView = inflater.inflate(R.layout.fragment_tab_0_upcoming, container, false);

        //View and Layout Manager
        myActivityList = (RecyclerView) upcomingView.findViewById(R.id.upcoming_recycleview);
        myActivityList.setLayoutManager(new LinearLayoutManager(getContext()));


        return upcomingView;
    }

    @Override
    public void onStart() {
        super.onStart();
        query_activities = db.collection("Activities");

        //Firestore RecyclerOptions Object with query
        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>().setQuery(query_activities, Activity.class).build();


        //RecyclerAdapter
        FirestoreRecyclerAdapter<Activity,ActivityViewHolder> adapter
                = new FirestoreRecyclerAdapter<Activity, ActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ActivityViewHolder holder, int position, @NonNull Activity model) {

                //Setting the text for each field in the upcoming_row (holder)
                holder.exercise_name.setText(model.getActivityName());
                holder.exercise_notes.setText(model.getInstructionalNotes());
                holder.exercise_reps.setText(model.getReps());
                holder.exercise_sets.setText(model.getSets());

            }

            @NonNull
            @Override
            public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                //Layoutinflater and viewholder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_row, parent, false);
                ActivityViewHolder viewHolder = new ActivityViewHolder(view);
                return viewHolder;
            }
        };


        //Setting adapter for list and start listening

        adapter.startListening();//TODO
        myActivityList.setAdapter(adapter);

    }



    //ViewHolder For the RecycleView
    public static class ActivityViewHolder extends RecyclerView.ViewHolder{

        //Initializing textview objects to be edited
        TextView exercise_name, exercise_reps,exercise_sets,exercise_notes;

        public ActivityViewHolder(@NonNull View itemView){
            super(itemView);

            //getting
            exercise_name = itemView.findViewById(R.id.exercise_name);
           exercise_reps = itemView.findViewById(R.id.exercise_reps);
            exercise_sets = itemView.findViewById(R.id.exercise_sets);
            exercise_notes = itemView.findViewById(R.id.exercise_notes);
        }

    }






}