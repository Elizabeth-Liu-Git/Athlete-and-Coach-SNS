package com.example.activitymonitor;

/**
 * @author Will Robbins
 * Espresso tests for the SendMessageActivity.java class.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SendMessageActivity_EspressoTest {

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    @Test
    public void test_activity_inView() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.msg_main)).check(matches(isDisplayed()));
    }

    @Test
    public void test_visibility_layouts() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.layout_chatbox)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_contacts)).check(matches(isDisplayed()));
    }

    @Test
    public void accessible_through_profile() {
        ActivityScenario<ProfilePage> activity = ActivityScenario.launch(ProfilePage.class);
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), SendMessageActivity.class);
        intent.putExtra("TEST_UID","0123456789");

        ActivityScenario<SendMessageActivity> activity2 = ActivityScenario.launch(SendMessageActivity.class);
        String uid = intent.getStringExtra("TEST_UID");

        assertEquals(uid, "0123456789");
    }

    /*
    Tests below rely on Athlete Test and Coach Test profiles created, with conversation started.
     */
    @Test
    public void open_spinner() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.contacts_spinner)).perform(click());
        SystemClock.sleep(4000);
    }

    @Test
    public void select_athlete_from_spinner() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.contacts_spinner)).perform(click());
        SystemClock.sleep(4000);

        onView(withText("Athlete Tester")).perform(click());
    }

    // Assumption: Conversation has only one previous message
    @Test
    public void send_message_to_athlete_from_coach() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.contacts_spinner)).perform(click());
        SystemClock.sleep(2000);

        onView(withText("Athlete Tester")).perform(click());

        onView(withId(R.id.edittext_chatbox)).perform(click());
        onView(withId(R.id.edittext_chatbox)).perform(typeText("This is a test message!"));

        onView(withId(R.id.recycler_view_messagelist))
                .check(new RecyclerViewItemCountAssertion(1));
        onView(withId(R.id.recycler_view_messagelist))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("How were the sprints?")), click()));

        onView(withId(R.id.button_chatbox_send)).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.recycler_view_messagelist))
                .check(new RecyclerViewItemCountAssertion(2));
        onView(withId(R.id.recycler_view_messagelist))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("This is a test message!")), click()));

    }


}