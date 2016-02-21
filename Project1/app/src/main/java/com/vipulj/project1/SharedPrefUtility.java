package com.vipulj.project1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by VJ on 21/02/16.
 */
public class SharedPrefUtility {

    public static final String KEY_FAVOURITE = "favourite";

    public static Set<String> getFavoriteMoview(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context
                .MODE_PRIVATE);
        return sharedPreferences.getStringSet(KEY_FAVOURITE, new HashSet<String>());
    }

    public static void setFavoriteMoview(Context context, Set<String> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_FAVOURITE, list);
        editor.commit();
    }
}
