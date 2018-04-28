package com.aktu.root.teachersassistant.student;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aktu.root.teachersassistant.FireStore;
import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.post_file.FileAdapter;
import com.aktu.root.teachersassistant.teacher.post_file.FileDataModel;
import com.aktu.root.teachersassistant.teacher.post_file.ManageAssignment;
import com.aktu.root.teachersassistant.teacher.post_file.ManageNotes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.aktu.root.teachersassistant.teacher.post_file.ManageAssignment.manageAssignmentList;
import static com.aktu.root.teachersassistant.teacher.post_file.ManageNotes.manageNotesList;

public class StudentViewAssignment extends AppCompatActivity {
    // Progress dialog type (0 - for Horizontal progress bar)
    private final int progress_bar_type = 0;
    private final String folder = "Teacher Assistant/Assignment";
    ArrayAdapter adapter;
    private ListView listView;
    private ProgressDialog pDialog;
    SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_assignment);

        Toolbar toolbar = findViewById(R.id.manageAssignmentToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mySwipeRefreshLayout = findViewById(R.id.manageAssignment_swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refereshContent();
            }
        });

//        listView = findViewById(R.id.manageAssignmentListview);
//
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ManageAssignment.list1);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                onClickAction(adapterView, view, i, l);
//            }
//        });
//        listView.setAdapter(adapter);


        recyclerView = findViewById(R.id.manageAssignmentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FileAdapter(manageAssignmentList, new FileAdapter.RecyclerViewClickListener() {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    private void refereshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FireStore.loadAssignmentFilesList();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentViewAssignment.this, StudentDashboard.class));
        finish();
        super.onBackPressed();
    }

    private void onClickAction(View view, final int i) {
        new DownloadFileFromURL(i).execute(manageAssignmentList.get(i).getFileUrl());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private int index;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        public DownloadFileFromURL(int i) {
            index = i;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());

                File myDir = new File(android.os.Environment.getExternalStorageDirectory(), folder);

            /* if specified not exist create new */
                if (!myDir.exists()) {
                    myDir.mkdir();
                    Log.v("", "inside mkdir");
                }
                String fname = manageAssignmentList.get(index).getFileName();
                File file = new File(myDir, fname);
                Log.d("file===========path", "" + file);
                if (file.exists())
                    file.delete();
                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
        }

    }
}
