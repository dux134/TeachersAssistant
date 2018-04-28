package com.aktu.root.teachersassistant;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aktu.root.teachersassistant.admin.AdminDashboard;
import com.aktu.root.teachersassistant.student.StudentDashboard;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.utils.CheckNetworkConnection;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public static FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private Button loginButton;
    private CheckBox admin, student, teacher;
    private TextView resetButton;
    private EditText inputEmail, inputPassword;
    private PrefManager prefManager;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    static DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("fcm");
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_login);
        prefManager = new PrefManager(getApplicationContext());
//        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        checkPermission();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            goToDashboard();
        }

        admin = findViewById(R.id.adminCheckbox);
        student = findViewById(R.id.studentCheckbox);
        teacher = findViewById(R.id.teacherCheckbox);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        resetButton = findViewById(R.id.reset);
        inputEmail.setText(prefManager.getEMAIL());

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAction();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student.setChecked(false);
                teacher.setChecked(false);
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.setChecked(false);
                student.setChecked(false);
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.setChecked(false);
                teacher.setChecked(false);
            }
        });

    }

    private boolean isCheckCheckboxChecked() {
        if (!student.isChecked() && !teacher.isChecked() && !admin.isChecked()) {
            Toast.makeText(getApplicationContext(), "please select any one Checkbox", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String whichCheckBoxChecked() {
        if (admin.isChecked())
            return "admin";
        else if (student.isChecked())
            return "student";
        else
            return "teacher";
    }

    private void goToDashboard() {

//        if(prefManager.getFIRSTLAUNCHED() == "yes") {
//            registerFCMToken();
//            prefManager.setFIRSTLAUNCHED("no");
//        }

        String userType = prefManager.getAccounttype();
        if (userType.equals("student")) {
            Intent intent = new Intent(Login.this, StudentDashboard.class);
            startActivity(intent);
        } else if (userType.equals("teacher")) {
            Intent intent = new Intent(Login.this, TeacherDashboard.class);
            startActivity(intent);
        } else if (userType.equals("admin")) {
            Intent intent = new Intent(Login.this, AdminDashboard.class);
            startActivity(intent);
        }
    }

    private void matchAccountType() {

        if (whichCheckBoxChecked().equals(prefManager.getAccounttype())) {
            goToDashboard();
        } else {
//            mAuth.signOut();
            progressDialog.dismiss();
            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(Login.this, R.style.Theme_AppCompat_DayNight_Dialog);
            builder.setCancelable(false);
            builder
                    .setMessage("You are not enrolled in '" + whichCheckBoxChecked() + "' account!!!")
                    .setPositiveButton("Login Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            inputPassword.setText("");
                            dialog.dismiss();
                            loginAction();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void loginAction() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        prefManager.setEMAIL(email);
        prefManager.setPASSWORD(password);

        if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isCheckCheckboxChecked())
            return;

        checkAccountTypeFromFirestore();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();


//                progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError("Password must be more than 6 digits");
                            } else {
                                Toast.makeText(Login.this, "Authentication Failed!" + task.toString(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), "" + mAuth.getCurrentUser(), Toast.LENGTH_LONG).show();
                            matchAccountType();

                        }
                    }
                });
    }

    private void checkAccountTypeFromFirestore() {
        prefManager.setAccounttype("");
        final String accountType = whichCheckBoxChecked()+"s";
        Login.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final DocumentReference docRef = FireStore.db.collection(accountType).document(inputEmail.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                prefManager.setAccounttype(document.get("profile_type").toString());
                                prefManager.setUsername(document.get("full_name").toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "try again!", Toast.LENGTH_SHORT).show();
//                                Login.mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
//                            Login.mAuth.signOut();
                        }
                    }
                });
            }
        });
    }

    private void checkPermission() {
        permissionStatus = this.getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

            } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getApplicationContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}