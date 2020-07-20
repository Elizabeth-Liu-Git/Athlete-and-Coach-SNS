package com.example.activitymonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymonitor.model.Activity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * tab_0_upcoming()
 * Tab that contains and displays the athlete's upcoming exercises from the firebase database
 */
public class tab_0_upcoming extends Fragment {

    private static final String TAG = "tab_0_upcoming";
    private View upcomingView;//View that contains upcoming exercises
    private RecyclerView myActivityList; //recyclerview that is populated with upcoming exercises from firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to Firestore cloud storage
    private String current_user_id = SignIn.USERID; //Current user's ID
    private databaseInteraction dataB = new databaseInteraction(db);

    Query query_activities; //Query that will find Activities that are assigned to user
    CollectionReference activities_collection;
    ArrayList<String> relevant_activity_ids = new ArrayList<String>();

    /**
     * tab_0_upcoming()
     * Empty constructor that is required for layout
     */
    public tab_0_upcoming() {
        // Required empty public constructor
    }

    /**
     *Start the exercise
     * @param activity, activity object to be passed through to start exercise
     */
    private void startEx(Activity activity) {
        Intent intent = new Intent(getContext(), StartExercise.class);
        intent.putExtra("ACTIVITY", activity);

        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Reference to Firestore Cloud storage and query for activities
        activities_collection = db.collection("Activities");


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

        ArrayList<String> one = new ArrayList<String>();

        dataB.readRelevantActivityIds(new AsynchCallback() {
            @Override
            public void onCallback(ArrayList<String> idList) {
                idList.add("");
                query_activities = activities_collection.whereIn("actId", idList);

                //Firestore RecyclerOptions Object with query of Activity objects
                FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>().setQuery(query_activities, Activity.class).build();

                //RecyclerAdapter that displays the Activities as specified
                FirestoreRecyclerAdapter<Activity,ActivityViewHolder> adapter
                        = new FirestoreRecyclerAdapter<Activity, ActivityViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ActivityViewHolder holder, int position, @NonNull Activity model) {

                        model.setDocumentId( this.getSnapshots().getSnapshot(position).getId() );
                        //Setting the text for each field in the upcoming_row (holder)
                        holder.exercise_name.setText(model.getActivityName());
                        holder.exercise_notes.setText(model.getInstructionalNotes());
                        holder.exercise_reps.setText(model.getReps());
                        holder.exercise_sets.setText(model.getSets());




                        //Activity Object passed through to the start exercise page
                        final Activity[] current_activity_to_pass ={model};

                        //Listener for the individual start exercise buttons
                        holder.start_exercise_button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                startEx(current_activity_to_pass[0]);
                            }
                        });
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
        });



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
        Button start_exercise_button;

        public ActivityViewHolder(@NonNull View itemView){
            //View Operations
            super(itemView);
            individualActivityView = itemView;

            //Retreiving each item to be populated by id
            exercise_name = itemView.findViewById(R.id.exercise_name);
            exercise_reps = itemView.findViewById(R.id.exercise_reps);
            exercise_sets = itemView.findViewById(R.id.exercise_sets);
            exercise_notes = itemView.findViewById(R.id.exercise_notes);
            start_exercise_button = itemView.findViewById(R.id.start_exercise_button);
            
        }

    }
}