package fsc.com.firebasedemo.service;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        System.out.println("hl------new token=" + s);
    }
}
