package com.example.activitymonitor;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymonitor.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Will Robbins
 *
 * CommentsPage: This page will allow athletes and coaches to create, view, and reply to comments
 * with respect to individual activities. The page will only display comments that are within a
 * coach-athlete relationship. Users can choose to create or reply to a comment within this page,
 * and will be redirected to the CreateComment page.
 */

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // TODO: 2020-06-10 Need to be sure about data model structure. This is for testing:
        Message testMessage = new Message("sender info","receiver info","test content");
        List<Message> testMsgList = new ArrayList<>();
        testMsgList.add(testMessage);

        mMessageRecycler = findViewById(R.id.recycler_view_messagelist);
        mMessageAdapter = new MessageListAdapter(this, testMsgList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

}
