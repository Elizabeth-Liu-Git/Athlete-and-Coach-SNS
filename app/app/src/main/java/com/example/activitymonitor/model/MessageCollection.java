package com.example.activitymonitor.model;

/**
 * @author Will Robbins
 * MessageCollection POJO
 * MessageCollection objects store Messages, and are only accessible by two users, who can
 * both be senders and receivers of messages. MessageCollections sort and display their Message
 * contents to the UI.
 */

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

public class MessageCollection {

    // Auto-generated ID for each MessageCollection
    @DocumentId
    private String CollectionID;

    private String userA, userB;

    public MessageCollection() {}

    public MessageCollection(String userA, String userB) {
        this.userA = userA;
        this.userB = userB;
    }

    @Exclude
    public String getCollectionID() {
        return CollectionID;
    }

    public void setCollectionID(String collectionID) {
        this.CollectionID = collectionID;
    }

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }
}