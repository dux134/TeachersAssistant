package com.aktu.root.teachersassistant.teacher.takeattendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.teacher.getuserinfo.GetStudentInfo;
import com.aktu.root.teachersassistant.teacher.getuserinfo.StudentDataModel;
import com.aktu.root.teachersassistant.utils.CheckNetworkConnection;

import java.util.ArrayList;

public class TakeAttendenceActivity extends AppCompatActivity {

    public static ArrayList<StudentDataModel> attendence = new ArrayList<>();
    SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button submit;
    private Spinner spinner;
    private String spinnerSelected;
    private String[] subjects = {"Database Management System", "Mathematics", "Operating System", "Automata", "Computer Network", "D.A.A.", "Compler Design",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        Toolbar toolbar = findViewById(R.id.takeAttendenceToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinner = findViewById(R.id.spinner);
        submit = findViewById(R.id.postAttendence);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = adapterView.getItemAtPosition(i).toString().trim();

                if (str.equals("Database Management System")) {
                    spinnerSelected = "dbms";
                } else if (str.equals("Mathematics")) {
                    spinnerSelected = "mathematics";
                } else if (str.equals("Operating System")) {
                    spinnerSelected = "os";
                } else if (str.equals("Automata")) {
                    spinnerSelected = "automata";
                } else if (str.equals("Computer Network")) {
                    spinnerSelected = "network";
                } else if (str.equals("D.A.A.")) {
                    spinnerSelected = "daa";
                } else if (str.equals("Compler Design")) {
                    spinnerSelected = "compiler";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(TakeAttendenceActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
                    FireStore.postAttendence(GetStudentInfo.allStudentDetails, spinnerSelected, getApplicationContext());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mySwipeRefreshLayout = findViewById(R.id.takeAttendence_swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refereshContent();
            }
        });

        recyclerView = findViewById(R.id.takeAttendenceRecylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new TakeAttendenceMyAdapter(GetStudentInfo.allStudentDetails, new TakeAttendenceMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean b = GetStudentInfo.allStudentDetails.get(position).getCheckbox();
                GetStudentInfo.allStudentDetails.get(position).setCheckbox(!b);
                //Toast.makeText(getApplicationContext(),""+GetStudentInfo.allStudentDetails.get(position).getCheckbox(), Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TakeAttendenceActivity.this, Login.class));
        finish();
    }

    private void refereshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FireStore.loadStudentsDetails();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
