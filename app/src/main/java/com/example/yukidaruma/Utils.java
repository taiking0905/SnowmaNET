package com.example.yukidaruma;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.os.Build;
import android.view.Window;


import androidx.appcompat.app.AppCompatActivity;

public class Utils extends AppCompatActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_BACKGROUND_COLOR = "backgroundColor";

    //背景反映させるためのコード
    // 背景色を設定
    public static void setBackgroundColor(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int backgroundColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE); // デフォルトは白
        View rootView = activity.findViewById(android.R.id.content);
        rootView.setBackgroundColor(backgroundColor);
    }

    // ステータスバーの色を設定
    public static void setStatusBarColor(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int statusBarColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE); // デフォルトは黒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(statusBarColor);
        }
    }

    // ナビゲーションバーの色を設定
    public static void setNavigationBarColor(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int navigationBarColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE); // デフォルトは黒
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setNavigationBarColor(navigationBarColor);
        }
    }

}
