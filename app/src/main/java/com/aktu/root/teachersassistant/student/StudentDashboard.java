package com.aktu.root.teachersassistant.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aktu.root.teachersassistant.Chat;
import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class StudentDashboard extends AppCompatActivity {

    public static TextView name, rollNo, mobile, email, attendencePercent, attendenceFraction, profileType, downloadDetails;
    public static TextView daa,os,comiler,automata,network,math,dbms,attendenceTitle;
    public static String atten;
    private final String folder = "Teacher Assistant";
    private CardView attendence,viewNotes,viewAssignment,disscussionForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);



        downloadDetails = findViewById(R.id.downloadDetails);

        viewAssignment = findViewById(R.id.studentAssignment);
        viewNotes = findViewById(R.id.studentsNotes);
        attendence = findViewById(R.id.attendance);
        disscussionForm = findViewById(R.id.studentDisscussion);

        name = findViewById(R.id.studentDashboardName);
        rollNo = findViewById(R.id.studentDashboardRollNo);
        mobile = findViewById(R.id.studentDashboardMobile);
        email = findViewById(R.id.studentDashboardEmail);
        attendenceFraction = findViewById(R.id.studentDashboardLectures);
        attendencePercent = findViewById(R.id.studentDashboardPercentage);
        profileType = findViewById(R.id.studentDashboardProfileType);
        downloadDetails.setText("Files will be Downloaded into : " + new File(android.os.Environment.getExternalStorageDirectory(), folder).toString());

//        os = findViewById(R.id.osAttendence);
//        daa = findViewById(R.id.daaAttendence);
//        network = findViewById(R.id.networkAttendence);
//        automata = findViewById(R.id.automataAttendence);
//        math = findViewById(R.id.mathAttendence);
//        dbms = findViewById(R.id.dbmsAttendence);
//        comiler = findViewById(R.id.compilerAttendence);
//        attendenceTitle = findViewById(R.id.attendenceTitle);
        loadDetails();

        viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentDashboard.this, StudentViewNotes.class));
                finish();
            }
        });
        viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentDashboard.this, StudentViewAssignment.class));
                finish();
            }
        });
        disscussionForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentDashboard.this,Chat.class));
                finish();
            }
        });

        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(StudentDashboard.this, R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setCancelable(false);
                builder
                        .setMessage("CURRENT ATTENDENCE\n\n" + atten)
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void loadDetails() {
        final PrefManager prefManager = new PrefManager(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FireStore.showStudentDetails(prefManager.getEMAIL(), getApplicationContext());
                FireStore.loadNotesFilesList();
                FireStore.loadAssignmentFilesList();

                File myDir = new File(android.os.Environment.getExternalStorageDirectory(), folder);
            /* if specified not exist create new */
                if (!myDir.exists()) {
                    myDir.mkdir();
                    Log.v("", "inside mkdir");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.updateYourAccount) {
            startActivity(new Intent(StudentDashboard.this, StudentUpdateDetails.class));
            finish();
            return true;
        }
        if (id == R.id.Sign_out_menu) {
            new PrefManager(this).setFIRSTLAUNCHED("yes");
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(StudentDashboard.this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
