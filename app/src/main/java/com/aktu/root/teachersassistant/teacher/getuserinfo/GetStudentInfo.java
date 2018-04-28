package com.aktu.root.teachersassistant.teacher.getuserinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;

import java.util.ArrayList;

public class GetStudentInfo extends AppCompatActivity {
    public static ArrayList<StudentDataModel> allStudentDetails = new ArrayList<>();
    SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student_info);

        Toolbar toolbar = findViewById(R.id.getStudentInfoToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mySwipeRefreshLayout = findViewById(R.id.getStudentInfo_swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refereshContent();
            }
        });

        recyclerView = findViewById(R.id.getStudentInfoRecylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new StudentMyAdapter(allStudentDetails);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(GetStudentInfo.this, Login.class));
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
