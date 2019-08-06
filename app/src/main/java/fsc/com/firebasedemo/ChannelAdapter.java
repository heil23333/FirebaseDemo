package fsc.com.firebasedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fsc.com.firebasedemo.bean.Channel;

public class ChannelAdapter extends RecyclerView.Adapter <ChannelAdapter.MyViewHolder> {

    private ArrayList<Channel> channels;
    private Context context;

    public ChannelAdapter(ArrayList<Channel> channels, Context context) {
        this.channels = channels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_channel, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.channelName.setText(channels.get(position).getChannelName());
        holder.msgContent.setText("测试数据啦 哈哈哈");
        holder.msgTime.setText("17:30");
    }

    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView channelName, msgTime, msgContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.channel_name);
            msgTime = itemView.findViewById(R.id.msg_time);
            msgContent = itemView.findViewById(R.id.msg_content);
        }
    }
}
