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

    private User UserA, UserB;

    public MessageCollection() {}

    public MessageCollection(User UserA, User UserB) {
        this.UserA = UserA;
        this.UserB = UserB;
    }

    @Exclude
    public String getCollectionID() {
        return CollectionID;
    }

    public void setCollectionID(String collectionID) {
        this.CollectionID = collectionID;
    }

    public User getUserA() {
        return UserA;
    }

    public void setUserA(User UserA) {
        this.UserA = UserA;
    }

    public User getUserB() {
        return UserB;
    }

    public void setUserB(User UserB) {
        this.UserB = UserB;
    }
}