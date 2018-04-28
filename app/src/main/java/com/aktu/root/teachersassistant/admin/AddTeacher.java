package com.aktu.root.teachersassistant.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.AddStudent;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.utils.CheckNetworkConnection;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class AddTeacher extends AppCompatActivity {

    Map<String, Object> teacherData = new HashMap<>();
    private EditText fullname,mobile,password,email;
    private Button submit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        Toolbar toolbar = findViewById(R.id.addStudentToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getEmail();
            Toast.makeText(getApplicationContext(), "" + name, Toast.LENGTH_SHORT).show();

        }

        fullname = (EditText)findViewById(R.id.addteacher_fullname);
        mobile = (EditText)findViewById(R.id.addteacher_mobile);
        password = (EditText)findViewById(R.id.addteacher_password);
        email = (EditText)findViewById(R.id.addteacher_email);
        submit = (Button)findViewById(R.id.addteacher_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setClickable(false);
                if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkDetails()) {
                    final ProgressDialog progressDialog = new ProgressDialog(AddTeacher.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    mAuth.signOut();
                    teacherData.put("full_name", fullname.getText().toString());
                    teacherData.put("mobile_no", mobile.getText().toString());
                    teacherData.put("email", email.getText().toString());
                    teacherData.put("profile_type", "teacher");
                    FireStore.createProfile(teacherData, "teachers", getApplicationContext());

                    addNewTeacher(email.getText().toString(), password.getText().toString());

                    final PrefManager prefManager = new PrefManager(getApplicationContext());

                    mAuth.signInWithEmailAndPassword(prefManager.getEMAIL(), prefManager.getPASSWORD())
                            .addOnCompleteListener(AddTeacher.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            //inputPassword.setError("Password must be more than 6 digits");
                                        } else {
                                            Toast.makeText(AddTeacher.this, "Failed!" + task.toString(), Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    } else {
                                        fullname.setText("");
                                        email.setText("");
                                        mobile.setText("");
                                        password.setText("");
//                                        Intent intent = new Intent(AddStudent.this, Dasboard.class);
//                                        startActivity(intent);
//                                        finish();
                                    }
                                    submit.setClickable(true);

                                }
                            });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 4000);

                }
                submit.setClickable(true);
            }
        });
    }

    private boolean checkDetails() {
        if (TextUtils.isEmpty(fullname.getText().toString())) {
            fullname.setError("Name cannot be Empty");
            return false;
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Email cannot be Empty");
            return false;
        } else if (TextUtils.isEmpty(mobile.getText().toString())) {
            mobile.setError("Mobile no. cannot be Empty");
            return false;
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Password cannot be Empty");
            return false;
        } else
            return true;
    }

    private void addNewTeacher(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getEmail();
                                Toast.makeText(getApplicationContext(), "" + name, Toast.LENGTH_SHORT).show();

                            }
                            mAuth.signOut();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Adding Student Failed!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddTeacher.this, AdminDashboard.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
