package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * tab_0_upcoming()
 * Tab that contains and displays the athlete's upcoming exercises from the firebase database
 */
public class tab_0_upcoming extends Fragment {

    private View upcomingView;//View that contains upcoming exercises
    private RecyclerView myActivityList; //recyclerview that is populated with upcoming exercises from firebase
    private FirebaseFirestore db; //Reference to Firestore cloud storage
    Query query_activities; //Query that will find Activities that are assigned to user

    /**
     * tab_0_upcoming()
     * Empty constructor that is required for layout
     */
    public tab_0_upcoming() {
        // Required empty public constructor
    }

    /**
     *Start the exercise
     */
    private void startEx() {
        Intent intent = new Intent(this, StartExercise.class);
        this.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Reference to Firestore Cloud storage and query for activities
        db = FirebaseFirestore.getInstance();
        query_activities = db.collection("Activities");//TODO make query specific to users

        // Inflate the layout for this fragment
        upcomingView = inflater.inflate(R.layout.fragment_tab_0_upcoming, container, false);

        //View and Layout Manager
        myActivityList = (RecyclerView) upcomingView.findViewById(R.id.upcoming_recycleview);
        myActivityList.setLayoutManager(new LinearLayoutManager(getContext()));

        //Return view
        return upcomingView;
    }

    /**
     * onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        query_activities = db.collection("Activities");

        //Firestore RecyclerOptions Object with query of Activity objects
        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>().setQuery(query_activities, Activity.class).build();

        //RecyclerAdapter that displays the Activities as specified
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

                //Layoutinflater and viewholder that are dynamically populated from FireStore
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_row, parent, false);
                ActivityViewHolder viewHolder = new ActivityViewHolder(view);
                return viewHolder;
            }
        };
        //Setting adapter for list and start listening
        adapter.startListening();
        myActivityList.setAdapter(adapter);
    }


    /**
     * ActivityViewHolder()
     * Specific viewholder that is used for the display of Activity objects in the RecyclerView
     * Allows for the recyclerview to be dynamically populated from the FireStore
     */
    public static class ActivityViewHolder extends RecyclerView.ViewHolder{

        View individualActivityView;

        //Initializing textview objects to be edited
        TextView exercise_name, exercise_reps,exercise_sets,exercise_notes;

        public ActivityViewHolder(@NonNull View itemView){
            //View Operations
            super(itemView);
            individualActivityView = itemView;

            //Retreiving each item to be populated by id
            exercise_name = itemView.findViewById(R.id.exercise_name);
            exercise_reps = itemView.findViewById(R.id.exercise_reps);
            exercise_sets = itemView.findViewById(R.id.exercise_sets);
            exercise_notes = itemView.findViewById(R.id.exercise_notes);
            
        }

    }
}