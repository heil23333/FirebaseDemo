package fsc.com.firebasedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


import fsc.com.firebasedemo.bean.Channel;

public class ChannelAdapter extends FirestoreAdapter <ChannelAdapter.MyViewHolder> {

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

    class MyViewHolder extends RecyclerView.ViewHolder{

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
            System.out.println("hl--------channel.getChannelName()=" + channel.getChannelName());
            channelName.setText(channel.getChannelName());
            msgContent.setText("测试数据啦 哈哈哈");
            msgTime.setText("17:30");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelSelected(snapshot);
                }
            });
        }
    }
}
