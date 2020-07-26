package com.techninjas.umeedforwomen.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static final String APP_AUTH = "APP_AUTH";

    // properties
    private static final String NAME = "NAME";
    private static final String ID = "ID";
    private static final String LANG = "LANG";

    private SharedPrefUtil() { }

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

    public static String getId(Context context) {
        return getSharedPreferences(context).getString(ID , null);
    }

    public static void setId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(ID , newValue);
        editor.apply();
    }

    public static void removeAll(Context context){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(NAME);
        editor.remove(ID);
        editor.apply();
    }

    public static void changeLanguage(Context context){
        if(getSharedPreferences(context).getString(LANG, null) == null) {
            final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LANG, "hi");
            editor.apply();
        }else{
            final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.remove(LANG);
            editor.apply();
        }
    }

}

