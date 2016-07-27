package com.example.dantesrevelion.mipedido.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pmruiz on 27/07/2016.
 */
public class SharedPreferencesUtils {
    public static String NAME_SHARE_PREFERENCES="configuracion_host";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void writeSharedPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readStringSharedPreference(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }


}
