package com.example.activitymonitor;

/**
 * @author Will Robbins
 * Espresso tests for the SendMessageActivity.java class.
 */

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SendMessageActivity_EspressoTest {

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
    public void test_messagebar_acceptsInput() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.edittext_chatbox)).perform(typeText("This is a test message!"));
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

    @Test
    public void sent_message_displays() {
        ActivityScenario<SendMessageActivity> activity = ActivityScenario.launch(SendMessageActivity.class);

        onView(withId(R.id.edittext_chatbox)).perform(typeText("bonjour"));
        onView(withId(R.id.button_chatbox_send)).perform(click());

    }
    
}