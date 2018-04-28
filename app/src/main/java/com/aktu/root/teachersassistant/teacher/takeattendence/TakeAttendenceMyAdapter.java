package com.aktu.root.teachersassistant.teacher.takeattendence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aktu.root.teachersassistant.R;
import com.aktu.root.teachersassistant.teacher.getuserinfo.StudentDataModel;

import java.util.ArrayList;

/**
 * Created by root on 2/26/18.
 */

public class TakeAttendenceMyAdapter extends RecyclerView.Adapter<TakeAttendenceMyAdapter.TakeAttendenceViewHolder> {
    public ArrayList<StudentDataModel> list;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;

    public TakeAttendenceMyAdapter(ArrayList<StudentDataModel> list1, RecyclerViewClickListener rv) {
        list = list1;
        recyclerViewClickListener = rv;
    }

    @Override
    public TakeAttendenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.take_attendence_recycler_item, parent, false);
        TakeAttendenceMyAdapter.TakeAttendenceViewHolder holder = new TakeAttendenceMyAdapter.TakeAttendenceViewHolder(view, recyclerViewClickListener);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(TakeAttendenceViewHolder holder, int position) {
        holder.roll.setText(list.get(position).getRollno());
        holder.name.setText(list.get(position).getFullName());
        holder.checkBox.setChecked(list.get(position).getCheckbox());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }

    public class TakeAttendenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerViewClickListener recyclerView_ClickListener;
        private TextView roll;
        private TextView name;
        private CheckBox checkBox;

        public TakeAttendenceViewHolder(View itemView, RecyclerViewClickListener r) {
            super(itemView);
            roll = itemView.findViewById(R.id.take_attendence_rollno);
            name = itemView.findViewById(R.id.take_attendence_name);
            checkBox = itemView.findViewById(R.id.take_attendence_checkBox);

            recyclerView_ClickListener = r;
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerView_ClickListener.onClick(itemView, getAdapterPosition());
        }
    }
}
