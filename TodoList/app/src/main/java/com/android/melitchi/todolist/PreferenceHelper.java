package com.android.melitchi.todolist;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fonta on 09/11/2016.
 */

public class PreferenceHelper {

    private static final String MY_PREF = "todo_pref";

    public static void setToken(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }
}
