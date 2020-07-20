package com.example.activitymonitor;

import com.example.activitymonitor.model.Message;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Will Robbins
 * Test suite for Message.java
 */

public class Message_UnitTest {

    Message msg = new Message();

    @Test
    public void setgetMessageID_test() {
        msg.setMessageID("abc123");
        assertEquals(msg.getMessageID(), "abc123");
    }

    @Test
    public void setgetSender_test() {
        msg.setSender("Will");
        assertEquals(msg.getSender(), "Will");
    }

    @Test
    public void setgetReceiver_test() {
        msg.setReceiver("Andrew");
        assertEquals(msg.getReceiver(), "Andrew");
    }

    @Test
    public void setgetContent_test() {
        msg.setContent("test message");
        assertEquals(msg.getContent(), "test message");
    }

    @Test
    public void argsConstructor_test() {
        Message msg2 = new Message("Will", "Andrew", "test message");
        assertEquals(msg2.getSender(), "Will");
        assertEquals(msg2.getReceiver(), "Andrew");
        assertEquals(msg2.getContent(), "test message");
    }

    // TODO: 2020-07-07 Test getTime method

}
