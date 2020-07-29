package com.example.activitymonitor;

import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Asynchcallback class
 * used to get data asynchronously for databaseinteractions
 */
public interface AsynchCallback {
    void onCallback(ArrayList<Object> resultList);



}