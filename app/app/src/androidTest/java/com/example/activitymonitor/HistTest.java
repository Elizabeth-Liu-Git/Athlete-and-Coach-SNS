package com.example.activitymonitor;

import org.junit.Test;

/**
 * Tests to ensure historicalActivity is saving and reading properly
 * ensure timer is changing from seconds to min correctly
 */
public class HistTest {

    HistoricalActivity activity = new HistoricalActivity("Test", "Usain", "Run fast", "5", "2", true, 600);


    @Test
    public void HistTestTimer() {
        if (activity.getTimer() != 10) throw new AssertionError("Timer doesn't match");

    }
    @Test
    public void HistTestName() {
        if (!activity.getAthleteName().equals("Usain")) throw new AssertionError("Name doesn't match");

    }
    @Test
    public void HistTestActivity() {
        if (!activity.getActivityName().equals("Test")) throw new AssertionError("Activity doesn't match");

    }
    @Test
    public void HistTestNotes() {
        if (!activity.getInstructionalNotes().equals("Run fast")) throw new AssertionError("Notes doesn't match");

    }
    @Test
    public void HistTestSets() {
        if (!activity.getSets().equals("2H")) throw new AssertionError("Sets doesn't match");

    }
    @Test
    public void HistTestReps() {
        if (!activity.getReps().equals("5")) throw new AssertionError("Reps doesn't match");

    }


}


