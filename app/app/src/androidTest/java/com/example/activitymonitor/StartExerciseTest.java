package com.example.activitymonitor;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.activitymonitor.StartExercise;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class StartExerciseTest {
    @Rule
    public ActivityScenarioRule<StartExercise> scenario1 = new ActivityScenarioRule<>(StartExercise.class);

    @Test
    public void startExercise() {

        onView(withId(R.id.start_btn_startexercise)).perform(click());

        assertTrue(StartExercise.currentlyExercising);
        assertFalse(StartExercise.doneExercise);

    }

    @Test
    public void pauseExercise() {

        onView(withId(R.id.pause_btn_startexercise)).perform(click());

        assertFalse(StartExercise.currentlyExercising);
        assertFalse(StartExercise.doneExercise);

    }



}
