package fsc.com.firebasedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


import java.text.SimpleDateFormat;
import java.util.Date;

import fsc.com.firebasedemo.R;
import fsc.com.firebasedemo.bean.Channel;

public class ChannelAdapter extends FirestoreAdapter<ChannelAdapter.MyViewHolder> {

    private OnChannelSelectedListener mListener;

    public interface OnChannelSelectedListener {

        void onChannelSelected(DocumentSnapshot channel);

    }

    public void setChannelSelectedListener(OnChannelSelectedListener listener) {
        this.mListener = listener;
    }

    public ChannelAdapter(Query query, OnChannelSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_channel, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView channelName, msgTime, msgContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.channel_name);
            msgTime = itemView.findViewById(R.id.msg_time);
            msgContent = itemView.findViewById(R.id.msg_content);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnChannelSelectedListener listener) {
            final Channel channel = snapshot.toObject(Channel.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            channelName.setText(channel.getChannelName());
            if (channel.getLastMsgContent().length() == 0) {
                msgContent.setText("还没有人聊天呢");
            } else {
                msgContent.setText(channel.getLastMsgContent());
            }
            msgTime.setText(format.format((new Date(channel.getChannelCreateTime()))));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelSelected(snapshot);
                }
            });
        }
    }
}
