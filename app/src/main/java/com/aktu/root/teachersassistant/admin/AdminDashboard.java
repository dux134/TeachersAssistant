package com.aktu.root.teachersassistant.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.aktu.root.teachersassistant.Chat;
import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.AddStudent;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.teacher.UpdateYourAccount;
import com.aktu.root.teachersassistant.teacher.getuserinfo.GetStudentInfo;
import com.aktu.root.teachersassistant.teacher.post_file.ManageAssignment;
import com.aktu.root.teachersassistant.teacher.post_file.ManageNotes;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {

    private Button addTeacher1;
    private CardView addTeacher,addStudent,manageNotes,manageAssignment,disscussion,studentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        loadDetails();
        addTeacher = findViewById(R.id.addteacher);
        studentInfo = findViewById(R.id.adminstudentdetail);
        addStudent = findViewById(R.id.adminaddStudent);
        manageAssignment = findViewById(R.id.adminmanageAssignment);
        manageNotes = findViewById(R.id.adminManageNotes);
        disscussion = findViewById(R.id.admindiscussion);

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this,AddTeacher.class));
                finish();
            }
        });
        disscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, Chat.class));
                finish();
            }
        });
        manageNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, ManageNotes.class));
                finish();
            }
        });
        manageAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, ManageAssignment.class));
                finish();
            }
        });
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddStudent.class));
                finish();
            }
        });
        studentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, GetStudentInfo.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    private void loadDetails() {
        final PrefManager prefManager = new PrefManager(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FireStore.loadStudentsDetails();
                FireStore.loadAssignmentFilesList();
                FireStore.loadNotesFilesList();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_dashboard_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.Sign_out_menu) {
            new PrefManager(this).setFIRSTLAUNCHED("yes");
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(AdminDashboard.this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
