package com.aktu.root.teachersassistant;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aktu.root.teachersassistant.utils.PrefManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        final PrefManager prefManager = new PrefManager(this);
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://teachersassistant-26f77.firebaseio.com/disscussion_form/");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", prefManager.getUsername());
                    reference1.push().setValue(map);
//                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(prefManager.getUsername())){
                    addMessageBox("You",message,1);
                }
                else{
                    addMessageBox(userName,message,2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String userName, String message,int type){

        CardView cardview = new CardView(this);
        CardView.LayoutParams layoutparams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(16,4,16,4);
        CardView cv = new CardView(this);
        cv.setLayoutParams(layoutparams);


        cardview.setLayoutParams(layoutparams);
        cardview.setRadius(35);
        cardview.setCardElevation(10);
        cardview.setMinimumWidth(width);
        cardview.setPadding(25, 25, 25, 25);
        if(type==1)
            cardview.setCardBackgroundColor(Color.DKGRAY);
        else
            cardview.setCardBackgroundColor(Color.GRAY);

        TextView textview1 = new TextView(this);
        textview1.setLayoutParams(layoutparams);
        textview1.setText(userName + " :-");
        textview1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        textview1.setTextColor(Color.WHITE);
        textview1.setPadding(25,25,25,25);
        textview1.setGravity(Gravity.LEFT);

        TextView textview2 = new TextView(this);
        textview2.setLayoutParams(layoutparams);
        textview2.setText("\n\n"+message);
        textview2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        textview2.setTextColor(Color.WHITE);
        textview2.setPadding(25,25,25,25);
        textview2.setGravity(Gravity.LEFT);

        cardview.addView(textview1);
        cardview.addView(textview2);
        layout.addView(cardview);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Chat.this,Login.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}