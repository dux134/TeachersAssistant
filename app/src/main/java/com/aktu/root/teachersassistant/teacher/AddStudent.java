package com.aktu.root.teachersassistant.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.utils.CheckNetworkConnection;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class AddStudent extends AppCompatActivity {

    Map<String, Object> studentData = new HashMap<>();
    private EditText name, mobile, password, email, rollno;
    private Button submit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

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

        name = findViewById(R.id.fullname);
        mobile = findViewById(R.id.addteacher_mobile);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        rollno = findViewById(R.id.take_attendence_rollno);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setClickable(false);
                if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkDetails()) {
                    final ProgressDialog progressDialog = new ProgressDialog(AddStudent.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    mAuth.signOut();
                    studentData.put("full_name", name.getText().toString());
                    studentData.put("roll_no", rollno.getText().toString());
                    studentData.put("mobile_no", mobile.getText().toString());
                    studentData.put("email", email.getText().toString());
                    studentData.put("profile_type", "student");
                    studentData.put("daa", "0/0");
                    studentData.put("os", "0/0");
                    studentData.put("mathematics", "0/0");
                    studentData.put("compiler", "0/0");
                    studentData.put("dbms", "0/0");
                    studentData.put("network", "0/0");
                    studentData.put("automata", "0/0");
                    FireStore.createProfile(studentData, "students", getApplicationContext());

                    addNewStudent(email.getText().toString(), password.getText().toString());

                    final PrefManager prefManager = new PrefManager(getApplicationContext());

                    mAuth.signInWithEmailAndPassword(prefManager.getEMAIL(), prefManager.getPASSWORD())
                            .addOnCompleteListener(AddStudent.this, new OnCompleteListener<AuthResult>() {
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
                                            Toast.makeText(AddStudent.this, "Failed!" + task.toString(), Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    } else {
                                        name.setText("");
                                        rollno.setText("");
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
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Name cannot be Empty");
            return false;
        } else if (TextUtils.isEmpty(rollno.getText().toString())) {
            rollno.setError("Roll No. cannot be Empty");
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

    private void addNewStudent(String email, String password) {
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
        startActivity(new Intent(AddStudent.this, Login.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
