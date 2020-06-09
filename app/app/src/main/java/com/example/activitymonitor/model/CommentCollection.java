package com.example.activitymonitor.model;

/**
 * @author Will Robbins
 * CommentCollection POJO
 * CommentCollection objects store messages, and are only accessible by two users, who can
 * both be senders and receivers of messages. MessageCollections sort and display their Message
 * contents to the UI.
 */

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

public class CommentCollection {

    @DocumentId
    private String documentID;

    private User UserA, UserB;
    // TODO: 2020-06-08 What data structure should be used for Firestore? Apparently only maps work

    public CommentCollection() {}

    public CommentCollection(User UserA, User UserB) {
        this.UserA = UserA;
        this.UserB = UserB;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
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