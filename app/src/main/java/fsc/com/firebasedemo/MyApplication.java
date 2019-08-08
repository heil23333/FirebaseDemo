package fsc.com.firebasedemo;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyApplication extends Application {
    private static FirebaseFirestore db;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static FirebaseFirestore getFirebaseDB() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }
}
