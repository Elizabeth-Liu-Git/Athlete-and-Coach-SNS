package com.example.activitymonitor;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

//THIS TEST MUST BE RUN WITH NO USER INSTANCE (NO SIGNED IN USER INFO)
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInTest {

    public static final String EMAIL_TO_BE_TYPED = "awesthaver13@gmail.com";
    public static final String PASSWORD_TO_BE_TYPED = "blahblahblah";

    @Rule public ActivityScenarioRule<SignIn> activityScenarioRule = new ActivityScenarioRule<>(SignIn.class);

    @Test
    public void changeviewProfilePage() throws InterruptedException {
        // Type text and then press the button
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(PASSWORD_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.buttonSubmit)).perform(click());

        //TODO Replace this with a more functional option
        Thread.sleep(1500);

        // Check that the view was changed.
        onView(withId(R.id.buttonSignOut)).check(matches(isDisplayed()));
    }
}