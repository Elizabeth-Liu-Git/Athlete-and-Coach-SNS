package com.example.activitymonitor;

import android.content.Intent;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssignExersiceTest {
    @Test
    public void test_selectExercise() {

        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.exercisedropdown)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.exercisedropdown)).check(matches(withSpinnerText(containsString("choose an exercise"))));

    }

    @Test
    public void test_selectAthlete() {

        ActivityScenario<AssignExercise> activity = ActivityScenario.launch(AssignExercise.class);
        SystemClock.sleep(1500);
        onView(withId(R.id.athlete)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.athlete)).check(matches(withSpinnerText(containsString("choose an athlete"))));
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
        onView(withId(R.id.exercisedropdown)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.exercisedropdown)).check(matches(withSpinnerText(containsString("test1"))));
        onView(withId(R.id.athlete)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.athlete)).check(matches(withSpinnerText(containsString("my name"))));
        onView(withId(R.id.NoteBox)).perform(typeText("This is a test note!"));
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.editText1)).perform(click());
        onView(withId(R.id.editText1)).perform(click());
    }

}
