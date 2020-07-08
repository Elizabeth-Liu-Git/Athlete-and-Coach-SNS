package com.example.activitymonitor;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

public class AssignExerciseTests {
    @Test
    public void test_selectExercise() {
        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(3000);
        final ArrayList<String> exercises = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String element2 = "choose an exercise";
        String newExercise = "create a new exercise";
        exercises.add(element2);
        exercises.add(newExercise);
        db.collection("Activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("ActivityName").toString();
                                String activityID = document.getId();
                                exercises.add(name);
                            }
                        }
                    }
                });
        for(int i = 0; i < exercises.size(); i++) {

            onView(withId(R.id.exercisedropdown)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            if(i == 1){
                Espresso.pressBack();
            }
            onView(withId(R.id.exercisedropdown)).check(matches(withSpinnerText(containsString(exercises.get(i)))));
        }

    }

    @Test
    public void test_selectAthlete() {
        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        final ArrayList<String> athletes = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        athletes.add("choose an athlete");
        db.collection("Users")
                .whereEqualTo("userType", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("firstName").toString() + " " + document.get("lastName").toString();
                                String userID = document.getId();
                                athletes.add(name);
                            }
                        }
                    }
                });
       for(int i = 0; i < athletes.size(); i++) {
           onView(withId(R.id.athlete)).perform(click());
           onData(anything()).atPosition(i).perform(click());
           onView(withId(R.id.athlete)).check(matches(withSpinnerText(containsString(athletes.get(i)))));
       }
    }

    @Test
    public void test_note_acceptsInput() {
        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        onView(withId(R.id.NoteBox)).perform(typeText("This is a test note!"));
    }

    @Test
    public void test_submitExerise() {

        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm"))
                .check(doesNotExist());
        onView(withId(R.id.exercisedropdown)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm"))
                .check(doesNotExist());
        onView(withId(R.id.athlete)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm"))
                .check(doesNotExist());
        onView(withId(R.id.NoteBox)).perform(typeText("This is a test note!"));
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm"))
                .check(doesNotExist());
        onView(withId(R.id.editText1)).perform(click());
        onView(withId(R.id.editText1)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm")).perform(click());
    }

    @Test
    public void test_submitExeriseWithNoExerciseSelected() {

        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.exercisedropdown)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.athlete)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.NoteBox)).perform(typeText("This is a test note!"));
        onView(withId(R.id.editText1)).perform(click());
        onView(withId(R.id.editText1)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withText("Confirm"))
                .check(doesNotExist());
    }

    @Test
    public void test_submitExeriseWithNoAthleteSelected() {

        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.exercisedropdown)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.athlete)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.NoteBox)).perform(typeText("This is a test note!"));
        onView(withId(R.id.editText1)).perform(click());
        onView(withId(R.id.editText1)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.button2)).perform(click());
        SystemClock.sleep(3000);
        onView(withText("Confirm"))
                .check(doesNotExist());
    }
    @Test
    public void test_dateFormat() {
        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.editText1)).perform(click());
        onView(withText("OK")).perform(click());
    }

}
