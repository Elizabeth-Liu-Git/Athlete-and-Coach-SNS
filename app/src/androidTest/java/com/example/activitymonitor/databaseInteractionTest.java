package com.example.activitymonitor;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class databaseInteractionTest {

    @Test
    public void saveExerciseData() {
        databaseLayer d = new databaseLayerMock();

        d.saveExerciseData("uid","id", 100, true);
    }

    @Test
    public void readRelevantActivityIds() {
        databaseLayer d = new databaseLayerMock();

        d.readRelevantActivityIds(new AsynchCallback() {
            @Override
            public void onCallback(ArrayList<Object> idList) {

            }
        });

    }

    @Test
    public void readRelevantAssignedActivities() {
        databaseLayer d = new databaseLayerMock();

        d.readRelevantActivityIds(new AsynchCallback() {
            @Override
            public void onCallback(ArrayList<Object> idList) {

            }
        });

    }

    @Test
    public void uniqueIntTest(){
        int a = databaseInteraction.getUniqueInteger("One String");
        int b = databaseInteraction.getUniqueInteger("Other String???");

        assertFalse(a==b);
    }

    class databaseLayerMock implements databaseLayer{
        public void saveExerciseData(String uid, String id, long time, boolean done) {
            return;
        }

        public void readRelevantActivityIds(AsynchCallback a){
            return;
        }

        public void readRelevantAssignedActivities(AsynchCallback a){
            return;
        }
    }
}


