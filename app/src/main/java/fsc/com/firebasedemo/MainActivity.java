package fsc.com.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fsc.com.firebasedemo.bean.Channel;

public class MainActivity extends AppCompatActivity {

    private TextView hintText;
    private RecyclerView channelRecyclerview;
    private ChannelAdapter adapter;
    private final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private Query query;
    private FirebaseFirestore firebaseFirestore;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build()
    );

    ChannelAdapter.OnChannelSelectedListener listener = new ChannelAdapter.OnChannelSelectedListener() {
        @Override
        public void onChannelSelected(DocumentSnapshot channel) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkLoginState();
    }

    private void checkLoginState() {
        if (mAuth != null && mAuth.getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(true)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private void initView() {
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("channel")
                .orderBy("channelName", Query.Direction.DESCENDING)
                .limit(50);

        hintText = findViewById(R.id.hint_text);
        channelRecyclerview = findViewById(R.id.channel_list);
        adapter = new ChannelAdapter(query, listener) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    channelRecyclerview.setVisibility(View.GONE);
                    hintText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("hl------getItemCount()=" + getItemCount());
                    channelRecyclerview.setVisibility(View.VISIBLE);
                    hintText.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                System.out.println("hl------error:" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        };
        channelRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        channelRecyclerview.setAdapter(adapter);
        channelRecyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        mAuth = FirebaseAuth.getInstance();
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
                checkLoginState();
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
                                    checkLoginState();
                                } else {
                                    Channel newChannel = new Channel(UUID.randomUUID().hashCode(), editText.getText().toString(), user.getUid());

                                    firebaseFirestore.collection("channel")
                                            .add(newChannel)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode != RESULT_OK) {
                if (response != null && response.getError() != null) {
                    response.getError().printStackTrace();
                } else {
                    finish();
                }
            }
        }
    }
}
