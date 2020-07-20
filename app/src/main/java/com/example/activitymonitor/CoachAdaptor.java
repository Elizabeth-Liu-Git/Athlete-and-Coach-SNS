package com.example.activitymonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This allows for the CoachCalender and PastActivity to allow for RecycleView
 */
public class CoachAdaptor extends RecyclerView.Adapter<CoachAdaptor.ViewHolder> {
    private ArrayList<HistoricalActivity> activityArrayList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Connects past_activity with PastActivity
     * @param holder
     * @param position - position in array
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoricalActivity activity = activityArrayList.get(position);

        holder.exerciseName.setText("Exercise Name: " + activity.getActivityName());
        holder.coachNotes.setText("Coach Notes: " + activity.getInstructionalNotes());
        holder.assignedAthlete.setText("Assigned Athlete: " + activity.getAthleteName());
        holder.completed.setText("Completed: " + activity.isComplete());
        holder.timer.setText("Time Elapsed: " + activity.getTimer());
    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseName;
        public TextView coachNotes;
        public TextView assignedAthlete;
        public TextView timer;
        public TextView completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.activityName);
            coachNotes = itemView.findViewById(R.id.Notes);
            assignedAthlete = itemView.findViewById(R.id.assignedAthlete);
            completed = itemView.findViewById(R.id.completed);
            timer = itemView.findViewById(R.id.timer);

        }
    }
    public CoachAdaptor (ArrayList<HistoricalActivity> arrayList){
        activityArrayList = arrayList;
    }
}
