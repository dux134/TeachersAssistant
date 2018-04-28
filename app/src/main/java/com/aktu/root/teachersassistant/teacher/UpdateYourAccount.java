package com.aktu.root.teachersassistant.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class UpdateYourAccount extends AppCompatActivity {
    private TextView teacherName, teacherMobile;
    private Button updateButton;
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_your_account);

        Toolbar toolbar = findViewById(R.id.updateYourAccountToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final PrefManager prefManager = new PrefManager(this);

        teacherName = (EditText) findViewById(R.id.updateYourAccountFullName);
        teacherMobile = (EditText) findViewById(R.id.updateYourAccountMobile);
        updateButton = findViewById(R.id.updateYourAccountButton);

        final ProgressDialog progressDialog = new ProgressDialog(UpdateYourAccount.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(teacherName.getText().toString())) {
                    teacherName.setError("Enter Name");
                    return;
                }
                if (TextUtils.isEmpty(teacherMobile.getText().toString())) {
                    teacherMobile.setError("Enter Mobile No.");
                    return;
                }

                progressDialog.show();
                DocumentReference contact = FireStore.db.collection("users").document(prefManager.getEMAIL());
                contact.update("full_name", teacherName.getText().toString().trim());
                contact.update("mobile_no", teacherMobile.getText().toString().trim());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 2000);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateYourAccount.this, TeacherDashboard.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
