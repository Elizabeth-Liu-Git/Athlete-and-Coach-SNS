package com.example.activitymonitor.model;

/**
 * @author Will Robbins
 * Comment POJO
 * Messages are stored by MessageCollections. They contain Sender and Receiver users, content
 * and a timestamp to record when the message was sent.
 */

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.time.LocalTime;

public class Comment {

    @DocumentId
    private String documentID;

    private User Sender, Receiver;
    private String Content;
    private LocalTime Time;

    public Comment() {}

    public Comment(User Sender, User Receiver, String Content) {
        this.Time = LocalTime.now();
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Content = Content;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User Sender) {
        this.Sender = Sender;
    }

    public User getReceiver() {
        return Receiver;
    }

    public void setReceiver(User Receiver) {
        this.Receiver = Receiver;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public LocalTime getTime() {
        return Time;
    }

    public void setTime(LocalTime Time) {
        this.Time = Time;
    }
}