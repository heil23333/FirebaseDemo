package fsc.com.firebasedemo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import fsc.com.firebasedemo.R;
import fsc.com.firebasedemo.adapter.ChatMessageAdapter;
import fsc.com.firebasedemo.bean.Message;

public class ChatActivity extends AppCompatActivity {

    private Query query;
    private Button sendBtn;
    private EditText msgEditText;
    private RecyclerView chatList;
    private ChatMessageAdapter adapter;
    private long channelUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
    }

    private void initView() {
        sendBtn = findViewById(R.id.send_btn);
        msgEditText = findViewById(R.id.msg_edittext);
        chatList = findViewById(R.id.msg_list);
        channelUuid = getIntent().getLongExtra("channelUuid", 0);
        query = FirebaseFirestore.getInstance().collection(channelUuid + "")
                .orderBy("msgSendTime", Query.Direction.ASCENDING)
                .limit(50);
        adapter = new ChatMessageAdapter(query) {
            @Override
            protected void onDataChanged() {

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                System.out.println("hl------error:" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        };
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatList.setAdapter(adapter);
        chatList.addItemDecoration(new DividerItemDecoration(ChatActivity.this, DividerItemDecoration.VERTICAL));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(msgEditText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                        , System.currentTimeMillis(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseFirestore.getInstance().collection(channelUuid + "").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                            msgEditText.setText("");
                        }
                    }
                });
            }
        });
    }
}
