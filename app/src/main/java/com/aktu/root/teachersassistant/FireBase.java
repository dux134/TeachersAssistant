package com.aktu.root.teachersassistant;

import android.content.Context;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by root on 4/5/18.
 */

public class FireBase {
    public static ArrayList<String> list = new ArrayList<>();


    public static void clearList() {
        list.clear();
    }

    public static void loadStudentsFCMToken(Context context) {
        Firebase.setAndroidContext(context);
        Firebase firebase = new Firebase("https://teachersassistant-26f77.firebaseio.com/fcm/student/");

        firebase.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String token = String.valueOf(map.get("token"));
                String userName = map.get("user").toString();
                list.add(token);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static void loadTeacherFCMToken(Context context) {
        Firebase.setAndroidContext(context);
        Firebase firebase = new Firebase("https://teachersassistant-26f77.firebaseio.com/fcm/teacher");

        firebase.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String token = String.valueOf(map.get("token"));
                String userName = map.get("user").toString();
                list.add(token);
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static ArrayList<String> getList() {
        return list;
    }
}
