package com.aktu.root.teachersassistant.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aktu.root.teachersassistant.Chat;
import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.getuserinfo.GetStudentInfo;
import com.aktu.root.teachersassistant.teacher.post_file.PostAssignment;
import com.aktu.root.teachersassistant.teacher.post_file.PostNotes;
import com.aktu.root.teachersassistant.teacher.takeattendence.TakeAttendenceActivity;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeacherDashboard extends AppCompatActivity {
    public static TextView teacherName, teacherMobile, teacherEmail;
    private static String token;
    FirebaseUser user;
    private CardView addStudent, getCurrentUser, takeAttendence, postAssignment, postNotes,
            discussionForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        loadDetails();
        teacherEmail = findViewById(R.id.teacherDashboardEmail);
        teacherMobile = findViewById(R.id.teacherDashboardMobileNo);
        teacherName = findViewById(R.id.teacherDashboardName);
        addStudent = findViewById(R.id.addStudentCard);
        getCurrentUser = findViewById(R.id.adminstudentdetail);
        takeAttendence = findViewById(R.id.addteacher);
        postAssignment = findViewById(R.id.adminmanageAssignment);
        postNotes = findViewById(R.id.adminManageNotes);
//        manageAssignment = findViewById(R.id.manageAssignment);
//        manageNotes = findViewById(R.id.manageNotes);
        discussionForm = (findViewById(R.id.admindiscussion));

        discussionForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, Chat.class));
                finish();
            }
        });

//
//

        postNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, PostNotes.class));
                finish();
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, AddStudent.class));
                finish();
            }
        });

        getCurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, GetStudentInfo.class));
                finish();
            }
        });

        takeAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, TakeAttendenceActivity.class));
                finish();
            }
        });

        postAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherDashboard.this, PostAssignment.class));
                finish();
            }
        });
    }

    private void loadDetails() {
        final PrefManager prefManager = new PrefManager(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FireStore.loadTeacherDetails(prefManager.getEMAIL(), TeacherDashboard.this);
                FireStore.loadStudentsDetails();
                FireStore.loadAssignmentFilesList();
                FireStore.loadNotesFilesList();
//                FireBase.loadStudentsFCMToken(getApplicationContext());

//                user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                    public void onComplete(@NonNull Task<GetTokenResult> task) {
//                        if (task.isSuccessful()) {
//                            token = task.getResult().getToken();
//                        }
//                    }
//                });
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
            startActivity(new Intent(TeacherDashboard.this, UpdateYourAccount.class));
            finish();
            return true;
        }
        if (id == R.id.Sign_out_menu) {
            new PrefManager(this).setFIRSTLAUNCHED("yes");
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(TeacherDashboard.this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

