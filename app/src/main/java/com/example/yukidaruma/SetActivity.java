package com.example.yukidaruma;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;


public class SetActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_BACKGROUND_COLOR = "backgroundColor";
    private static final String KEY_GROWTH_RATE = "growthRate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        // SharedPreferencesの初期化　永続的にデータを保持
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 背景色を保存しておくための初期色 (白)
        int currentBackgroundColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE);

        // 初期の背景色を設定
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(currentBackgroundColor);

        // 背景色変更ボタン
        Button buttonChangeBackground = findViewById(R.id.button_change_background);
        buttonChangeBackground.setOnClickListener(v -> {
            // 現在の背景色を取得
            int currentColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE);// 初期値 白

            // 色を切り替え (空色 #87CEEB と白)
            int newColor = (currentColor == Color.WHITE) ? Color.parseColor("#87CEEB") : Color.WHITE;

            // SharedPreferencesに新しい色を保存
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_BACKGROUND_COLOR, newColor);
            editor.apply();  // 保存

            // 背景色を変更
            rootView.setBackgroundColor(newColor);

            // ステータスバーの色を変更　同じ色で変更
            Utils.setStatusBarColor(this);

            // ナビゲーションバーの色を変更
            Utils.setNavigationBarColor(this);

            // トーストで通知
            String colorName = (newColor == Color.parseColor("#87CEEB")) ? "空色" : "白";
            Toast.makeText(this, "背景色を " + colorName + " に変更しました", Toast.LENGTH_SHORT).show();
        });

        // 成長速度調整SeekBar
        SeekBar seekBarGrowthRate = findViewById(R.id.seekbar_growth_rate);
        // 初期値の取得時に getFloat を使用
        float savedGrowthRate = sharedPreferences.getFloat(KEY_GROWTH_RATE, 0.01f); // 初期値 0.1
        int progress = Math.round((savedGrowthRate - 0.01f) * 10000); // 0.001 ～ 0.02 を 0 ～ 190 にマッピング
        seekBarGrowthRate.setProgress(progress);

        seekBarGrowthRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress を成長速度に変換（0.1 ～ 2.0の範囲）
                float  growthRate = progress / 10000f + 0.001f;  // 0.001 ～ 0.020
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(KEY_GROWTH_RATE, growthRate);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float  growthRate = seekBar.getProgress() / 10000f + 0.001f; // 0.001 ～ 0.02
                Toast.makeText(SetActivity.this, "成長速度を " + String.format("%.4f", growthRate) + " に設定しました", Toast.LENGTH_SHORT).show();
            }

        });

        // 画像削除ボタン
        Button buttonDeleteImages = findViewById(R.id.button_delete_images);
        buttonDeleteImages.setOnClickListener(v -> showDeleteAllConfirmationDialog());


    }
    /**
     * 全画像削除確認ダイアログの表示
     */
    private void showDeleteAllConfirmationDialog() {
        // カスタムレイアウトを読み込む
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);

        // 各ビューを取得
        TextView dialogMessage = customView.findViewById(R.id.dialog_message);
        Button buttonPositive = customView.findViewById(R.id.button_positive);
        Button buttonNegative = customView.findViewById(R.id.button_negative);

        // メッセージの設定
        dialogMessage.setText("すべての画像を削除しますか？");

        // ダイアログを作成
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(customView)
                .create();

        // ボタンのクリックイベント
        buttonPositive.setOnClickListener(v -> {
            int deletedCount = deleteAllImages();
            if (deletedCount > 0) {
                Toast.makeText(this, deletedCount + " 個の画像を削除しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "削除する画像がありません", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        buttonNegative.setOnClickListener(v -> dialog.dismiss());

        // ダイアログの背景をカスタム背景に設定
        dialog.setOnShowListener(d -> {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        });

        // ダイアログを表示
        dialog.show();
    }


    /**
     * すべての画像を削除する
     */
    private int deleteAllImages() {
        File filesDir = getFilesDir();
        File[] files = filesDir.listFiles();
        int deleteCount = 0;

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png") && file.delete()) {
                    deleteCount++;
                }
            }
        }

        return deleteCount;
    }
    /**
     * 前の画面に戻る処理
     */
    public void ClickBack(View view) {
        // アクティビティを終了して前の画面に戻る
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 背景色を再適用
        Utils.setBackgroundColor(this);
        // ステータスバーの色を変更
        Utils.setStatusBarColor(this);

        // ナビゲーションバーの色を変更
        Utils.setNavigationBarColor(this);
    }

}
