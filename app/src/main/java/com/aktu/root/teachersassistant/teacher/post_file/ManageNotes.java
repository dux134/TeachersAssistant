package com.aktu.root.teachersassistant.teacher.post_file;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.Login;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.teacher.getuserinfo.StudentMyAdapter;
import com.aktu.root.teachersassistant.utils.CheckNetworkConnection;

import java.util.ArrayList;

public class ManageNotes extends AppCompatActivity {

    public static ArrayList<FileDataModel> manageNotesList = new ArrayList<>();
    SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notes);

        Toolbar toolbar = findViewById(R.id.manageNotesToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mySwipeRefreshLayout = findViewById(R.id.manageNotes_swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refereshContent();
            }
        });


        recyclerView = findViewById(R.id.manageNotesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FileAdapter(manageNotesList, new FileAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                onClickAction(view,position);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ManageNotes.this, Login.class));
        finish();
        super.onBackPressed();
    }

    private void refereshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FireStore.loadNotesFilesList();
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

    private void onClickAction(View view, final int i) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(ManageNotes.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setCancelable(false);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
                            FireStore.deleteFile("notes", manageNotesList.get(i).getFileName(), ManageNotes.this);
                            refereshContent();
                        } else
                            Toast.makeText(ManageNotes.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
