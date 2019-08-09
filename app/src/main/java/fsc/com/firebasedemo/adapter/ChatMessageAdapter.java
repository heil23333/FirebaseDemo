package fsc.com.firebasedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

import fsc.com.firebasedemo.R;
import fsc.com.firebasedemo.bean.Message;

public class ChatMessageAdapter extends FirestoreAdapter<ChatMessageAdapter.MsgViewHolder> {
    public ChatMessageAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        TextView msgContent, msgTime;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            msgContent = itemView.findViewById(R.id.msg_content);
            msgTime = itemView.findViewById(R.id.msg_time);
        }

        public void bind(final DocumentSnapshot snapshot) {
            final Message message = snapshot.toObject(Message.class);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            msgTime.setText(format.format((new Date(message.getMsgSendTime()))));
            if (user != null && message.getMsgFromId().equals(user.getUid())) {
                msgContent.setText(String.format("我：%s", message.getMsgContent()));
            } else {
                msgContent.setText(String.format("%s：%s", message.getMsgFrom(), message.getMsgContent()));
            }
        }
    }
}
