package fsc.com.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fsc.com.firebasedemo.bean.Channel;

public class MainActivity extends AppCompatActivity {

    private TextView hintText;
    private RecyclerView channelRecyclerview;
    private ArrayList<Channel> channels;
    private ChannelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        hintText = findViewById(R.id.hint_text);
        channelRecyclerview = findViewById(R.id.channel_list);
        channels = new ArrayList<>();
        adapter = new ChannelAdapter(channels, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        channelRecyclerview.setLayoutManager(linearLayoutManager);
        channelRecyclerview.setAdapter(adapter);

        MyApplication.getFirebaseDB().collection("channel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    channels.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Channel channel = new Channel((long) documentSnapshot.get("channelNo"), documentSnapshot.getString("channelName"),
                                documentSnapshot.getString("channelCreator"));
                        channels.add(channel);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.create_channel:
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setView(editText)
                        .setTitle("请输入频道名称")
                        .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                    return;
                                } else {
                                    Map<String, Object> channel = new HashMap<>();
                                    channel.put("channelCreator", user.getUid());
                                    channel.put("channelNo", UUID.randomUUID().hashCode());
                                    channel.put("channelName", editText.getText().toString());
                                    MyApplication.getFirebaseDB().collection("channel")
                                            .add(channel)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(MainActivity.this, "创建成功："
                                                            + editText.getText().toString(), Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("hl------添加失败");
                                                    e.printStackTrace();
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
