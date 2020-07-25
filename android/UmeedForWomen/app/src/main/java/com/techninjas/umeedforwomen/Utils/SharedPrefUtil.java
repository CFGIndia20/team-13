package com.techninjas.umeedforwomen.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static final String APP_AUTH = "APP_AUTH";

    // properties
    private static final String NAME = "NAME";
    // other properties...


    private SharedPrefUtil() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_AUTH, Context.MODE_PRIVATE);
    }

    public static String getName(Context context) {
        return getSharedPreferences(context).getString(NAME , null);
    }

    public static void setName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(NAME , newValue);
        editor.apply();
    }

    public static void removeName(Context context){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(NAME);
        editor.apply();
    }

}

