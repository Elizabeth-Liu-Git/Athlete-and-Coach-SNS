package com.example.activitymonitor;

import com.example.activitymonitor.model.MessageCollection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Will Robbins
 * Test suite for MessageCollection.java
 */

public class MessageCollection_UnitTest {

    MessageCollection msgCo = new MessageCollection("Will", "Andrew");

    @Test
    public void getUserAB_test() {
        assertEquals(msgCo.getUserA(), "Will");
        assertEquals(msgCo.getUserB(), "Andrew");
    }

    @Test
    public void setUserAB_test() {
        msgCo.setUserA("Andrew");
        msgCo.setUserB("Will");

        assertEquals(msgCo.getUserB(), "Will");
        assertEquals(msgCo.getUserA(), "Andrew");
    }

    @Test
    public void setgetCollectionID_test() {
        msgCo.setCollectionID("abc123");
        assertEquals(msgCo.getCollectionID(), "abc123");
    }

    @Test
    public void MsgListEmptyInitially_test() {
        assertEquals(msgCo.getMsgList().size(), 0);
    }

    @Test
    public void addMessage_test() {
        msgCo.addMessage("test message", "Will", "Andrew");
        String content = msgCo.getMsgList().get(0).getContent();
        String sender = msgCo.getMsgList().get(0).getSender();
        String receiver = msgCo.getMsgList().get(0).getReceiver();

        assertEquals(msgCo.getMsgList().size(), 1);
        assertEquals(content, "test message");
        assertEquals(sender, "Will");
        assertEquals(receiver, "Andrew");
    }

}
