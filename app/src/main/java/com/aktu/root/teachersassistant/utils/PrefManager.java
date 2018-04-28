package com.aktu.root.teachersassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by root on 1/19/18.
 */

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "teacherModel";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String ACCOUNTTYPE = "AccountType";
    private static final String USERNAME = "UserName";
    private static final String TOKEN = "token";
    private static final String FIRSTLAUNCHED = "firstLaunched";
    static SharedPreferences pref;
    //PreferenceManager.getDefaultSharedPreferences()
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();
    }

    public static SharedPreferences getPref() {
        return pref;
    }

    public String getEMAIL() {
        return pref.getString(EMAIL, "");
    }

    public void setEMAIL(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getPASSWORD() {
        return pref.getString(PASSWORD, "");
    }

    public void setPASSWORD(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getAccounttype() {
        return pref.getString(ACCOUNTTYPE, "");
    }

    public void setAccounttype(String accounttype) {
        editor.putString(ACCOUNTTYPE, accounttype);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(USERNAME, "");
    }

    public void setUsername(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public String getTOKEN() {
        return pref.getString(TOKEN, "");
    }

    public void setTOKEN(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getFIRSTLAUNCHED() {
        return pref.getString(FIRSTLAUNCHED, "1");
    }

    public void setFIRSTLAUNCHED(String firstlaunched) {
        editor.putString(FIRSTLAUNCHED, firstlaunched);
        editor.commit();
    }
}