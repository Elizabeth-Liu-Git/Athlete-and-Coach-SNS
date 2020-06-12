package com.example.activitymonitor;

/**
 * @author Will Robbins
 * Adapter which will populate the drop-down menu with contacts to select for messaging
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymonitor.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.zip.CheckedOutputStream;

import io.opencensus.internal.Utils;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;

    public MessageListAdapter(Context mContext, List<Message> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View view) {
            super(view);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {

            messageText.setText(message.getContent());
            timeText.setText(message.getTime().toString());
            nameText.setText(message.getSender());
            // TODO: 2020-06-10 Set profile image
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageText, timeText;

        SentMessageHolder(View view) {
            super(view);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {

            messageText.setText(message.getContent());
            timeText.setText(message.getTime().toString());
        }
    }

    @Override
    public int getItemViewType(int pos) {
        Message message = mMessageList.get(pos);
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (message.getSender().equals(currentUserID)) {

            return VIEW_TYPE_MESSAGE_SENT;
        } else {

            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);

            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);

            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        Message message = mMessageList.get(pos);

        if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT) {

            ((SentMessageHolder) holder).bind(message);

        } else if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED) {

            ((ReceivedMessageHolder) holder).bind(message);

        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
