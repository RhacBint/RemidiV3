package com.example.remidiv3.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static final String PREF_NAME = "DosenSession";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSession(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("logged_in", loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean("logged_in", false);
    }

    public void setUsername(String username) {
        editor.putString("username", username).apply();
    }

    public String getUsername() {
        return pref.getString("username", null);
    }

    public void setIdUser(int idUser) {
        editor.putInt("id_user", idUser).apply();
    }

    public int getIdUser() {
        return pref.getInt("id_user", -1);
    }

    public void setRole(String role) {
        editor.putString("role", role).apply();
    }

    public String getRole() {
        return pref.getString("role", "");
    }
}
