package com.aktu.root.teachersassistant.teacher.getuserinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aktu.root.teachersassistant.R;

import java.util.ArrayList;

/**
 * Created by root on 2/26/18.
 */

public class StudentMyAdapter extends RecyclerView.Adapter<StudentMyAdapter.StudentViewHolder> {
    public ArrayList<StudentDataModel> list;
    Context context;

    public StudentMyAdapter(ArrayList<StudentDataModel> student_details) {
        list = student_details;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_student_info_recycler_item, parent, false);
        StudentMyAdapter.StudentViewHolder holder = new StudentMyAdapter.StudentViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        holder.email.setText("Email id : " + list.get(position).getEmailId());
        holder.mobile.setText("Mobile No. : " + list.get(position).getMobileno());
        holder.rollno.setText(list.get(position).getRollno());
        holder.name.setText("Full Name : " + list.get(position).getFullName());

        holder.totalAtten.setText("Daa(" + list.get(position).getDaa1() + "), Ddms(" + list.get(position).getDbms1() + "), Computer Network(" + list.get(position).getNetwork1() + "), Compiler Design("
                + list.get(position).getCompiler1() + "), Mathematics(" + list.get(position).getMathematics1() + "), Operating System(" + list.get(position).getOs1() + "), Automata(" + list.get(position).getAutomata1() + ")");

        String[] daa = list.get(position).getDaa1().split("/");
        String[] os = list.get(position).getOs1().split("/");
        String[] automata = list.get(position).getAutomata1().split("/");
        String[] compiler = list.get(position).getCompiler1().split("/");
        String[] network = list.get(position).getNetwork1().split("/");
        String[] mathematics = list.get(position).getMathematics1().split("/");
        String[] dbms = list.get(position).getDbms1().split("/");

        int present = Integer.parseInt(daa[0]) + Integer.parseInt(automata[0]) + Integer.parseInt(os[0]) + Integer.parseInt(dbms[0]) + Integer.parseInt(compiler[0]) + Integer.parseInt(mathematics[0]) + Integer.parseInt(network[0]);
        int total = Integer.parseInt(daa[1]) + Integer.parseInt(automata[1]) + Integer.parseInt(os[1]) + Integer.parseInt(dbms[1]) + Integer.parseInt(compiler[1]) + Integer.parseInt(mathematics[1]) + Integer.parseInt(network[1]);
        int percent = (int) ((Double.valueOf(present) / Double.valueOf(total)) * 100);
        holder.percentage.setText(percent + "% (" + present + "/" + total + ")");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView rollno;
        private TextView name;
        private TextView mobile;
        private TextView email;
        private TextView percentage;
        private TextView atten;
        private TextView totalAtten;

        public StudentViewHolder(View itemView) {
            super(itemView);
            rollno = itemView.findViewById(R.id.take_attendence_rollno);
            name = itemView.findViewById(R.id.take_attendence_name);
            mobile = itemView.findViewById(R.id.addteacher_mobile);
            email = itemView.findViewById(R.id.recyler_view_email);
            percentage = itemView.findViewById(R.id.percentage);
            totalAtten = itemView.findViewById(R.id.totalAttendence);

        }
    }
}
