package com.aktu.root.teachersassistant.teacher.getuserinfo;

/**
 * Created by root on 2/26/18.
 */

public class StudentDataModel {
    private String name, email;
    private String mobileno, rollno, daa1, network1, os1, mathematics1, dbms1, compiler1, automata1;
    private boolean checkbox;

    public StudentDataModel(String roll, String nam, String mob, String emai, String daa, String network, String os, String mathematics, String dbms, String compiler, String automata) {
        rollno = roll;
        name = nam;
        mobileno = mob;
        email = emai;
        daa1 = daa;
        network1 = network;
        os1 = os;
        mathematics1 = mathematics;
        dbms1 = dbms;
        compiler1 = compiler;
        automata1 = automata;
        checkbox = false;

    }

    public String getEmailId() {
        return email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getRollno() {
        return rollno;
    }

    public String getFullName() {
        return name;
    }

    public String getAutomata1() {
        return automata1;
    }

    public String getCompiler1() {
        return compiler1;
    }

    public String getDaa1() {
        return daa1;
    }

    public String getDbms1() {
        return dbms1;
    }

    public String getMathematics1() {
        return mathematics1;
    }

    public String getNetwork1() {
        return network1;
    }

    public String getOs1() {
        return os1;
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
