package com.aktu.root.teachersassistant.teacher.post_file;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.getuserinfo.StudentMyAdapter;
import com.aktu.root.teachersassistant.teacher.takeattendence.TakeAttendenceMyAdapter;

import java.util.ArrayList;

/**
 * Created by root on 3/26/18.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    public static ArrayList<FileDataModel> list = new ArrayList<>();
    private Context context;
    FileAdapter.RecyclerViewClickListener recyclerViewClickListener;

    public FileAdapter(ArrayList<FileDataModel> fileDataModel,RecyclerViewClickListener rv) {
        list = fileDataModel;
        recyclerViewClickListener = rv;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_recycler_iten, parent, false);
        FileAdapter.FileViewHolder holder = new FileAdapter.FileViewHolder(view,recyclerViewClickListener);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        holder.fileName.setText(list.get(position).getFileName());
        holder.fileDescription.setText(list.get(position).getFileDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView fileName;
        private TextView fileDescription;
        RecyclerViewClickListener mListener1;

        public FileViewHolder(View itemView, RecyclerViewClickListener ml) {
            super(itemView);

            mListener1 = ml;
            fileDescription = itemView.findViewById(R.id.fileDescription);
            fileName = itemView.findViewById(R.id.fileTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
         mListener1.onClick(view,getAdapterPosition());
        }
    }
    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }
}
