package com.aktu.root.teachersassistant.student;

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

public class StudentUpdateDetails extends AppCompatActivity {
    private TextView studentName, studentMobile,rollno;
    private Button updateButton;
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_update_account);

        Toolbar toolbar = findViewById(R.id.studentupdateYourAccountToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final PrefManager prefManager = new PrefManager(this);

        studentName = (EditText) findViewById(R.id.StudentupdateYourAccountFullName);
        studentMobile = (EditText) findViewById(R.id.studentupdateYourAccountMobile);
        rollno = (EditText)findViewById(R.id.studentupdateRollno);
        updateButton = findViewById(R.id.studentupdateYourAccountButton);

        final ProgressDialog progressDialog = new ProgressDialog(StudentUpdateDetails.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(studentName.getText().toString())) {
                    studentName.setError("Enter Name");
                    return;
                }
                if (TextUtils.isEmpty(studentMobile.getText().toString())) {
                    studentMobile.setError("Enter Mobile No.");
                    return;
                }

                progressDialog.show();
                DocumentReference contact = FireStore.db.collection("students").document(prefManager.getEMAIL());
                contact.update("full_name", studentName.getText().toString().trim());
                contact.update("mobile_no", studentMobile.getText().toString().trim());
                contact.update("roll_no", rollno.getText().toString().trim());

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
        super.onBackPressed();
        startActivity(new Intent(StudentUpdateDetails.this, StudentDashboard.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
