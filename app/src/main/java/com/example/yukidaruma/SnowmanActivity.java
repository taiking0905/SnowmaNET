package com.example.yukidaruma;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SnowmanActivity extends AppCompatActivity {

    private SnowManView snowManView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snowman);

        // SnowManView を取得
        snowManView = findViewById(R.id.snowManView);

        // Intent Mainからデータを取得
        float snowballSize = getIntent().getFloatExtra("snowball_size", 100.0f); // デフォルト値を設定
        int snowballColor = getIntent().getIntExtra("snowball_color", 0xFF00FF00); // デフォルト値を設定

        // SnowManView にデータを設定
        snowManView.setSnowflakeAttributes(snowballSize, snowballColor);

        // 上に動かすアニメーション
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(snowManView, "translationY", snowManView.getTranslationY(), snowManView.getTranslationY() - 100);
        moveUp.setDuration(1000); // 1秒で上に動かす

        // 下に動かすアニメーション
        ObjectAnimator moveDown = ObjectAnimator.ofFloat(snowManView, "translationY", snowManView.getTranslationY() - 100, snowManView.getTranslationY());
        moveDown.setDuration(1000); // 1秒で下に動かす

        // アニメーションを連続して実行する
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveUp, moveDown); // 上に動かしてから下に動かす
        animatorSet.setStartDelay(500); // 少し遅れて開始（オプション）
        animatorSet.start(); // アニメーション開始

        // データを TextView に表示する例
        TextView textView = findViewById(R.id.text_snowflake_info);
        String info = "サイズ: " + snowballSize + "\n色: #" + Integer.toHexString(snowballColor);
        textView.setText(info);

    }

    /**
     * 保存ボタンがクリックされた時の処理
     */
    public void ClickSave(View view) {
        // SnowManView からビットマップを取得
        Bitmap snowmanBitmap = snowManView.getSnowmanBitmap();

        // 内部ストレージに保存
        saveSnowmanToInternalStorage(snowmanBitmap);
    }

    /**
     * 雪だるまのBitmapを内部ストレージに保存するメソッド
     */
    private void saveSnowmanToInternalStorage(Bitmap snowmanBitmap) {
        // 日本時間を取得　画像の保存に時間を使う
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));

        // 現在の時刻を日本時間でフォーマット
        String formattedDate = sdf.format(new Date());

        // ファイル名を作成　例snowman_20250123_121033
        String fileName = "snowman_" + formattedDate + ".png";

        System.out.println("ファイル名: " + fileName);

        // ファイル出力用のストリーム
        FileOutputStream fos = null;

        try {
            // アプリの内部ストレージにファイルを保存 /data/data/com.example.yukidaruma/files/
            fos = openFileOutput(fileName, MODE_PRIVATE);
            snowmanBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            // 保存成功メッセージを表示
            Toast.makeText(this, "雪だるまを保存しました: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // 保存失敗メッセージを表示
            Toast.makeText(this, "保存に失敗しました", Toast.LENGTH_SHORT).show();
        }

        // ダイアログを表示して次のアクションを問う
        showNextActionDialog();
    }

    /**
     * 次のアクションを問うダイアログを表示
     */
    private void showNextActionDialog() {
        // カスタムレイアウトを読み込む
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);

        // 各ビューを取得
        TextView dialogMessage = customView.findViewById(R.id.dialog_message);
        Button buttonHome = customView.findViewById(R.id.button_positive);
        Button buttonSettings = customView.findViewById(R.id.button_negative);

        // メッセージの設定
        dialogMessage.setText("次にどちらに移動しますか？");

        // ボタンの名前を設定
        buttonHome.setText("検索に移動");
        buttonSettings.setText("コレクションに移動");

        // ダイアログを作成
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(customView)
                .create();

        // ボタンのクリックイベント
        buttonHome.setOnClickListener(v -> {
            // 検索に移動
            Intent homeIntent = new Intent(this, MainActivity.class); // HomeActivity は適切なアクティビティに変更
            startActivity(homeIntent);
            dialog.dismiss();
        });

        buttonSettings.setOnClickListener(v -> {
            // コレクション画面に移動
            Intent settingsIntent = new Intent(this, CollectionActivity.class);
            startActivity(settingsIntent);
            dialog.dismiss();
        });

        // ダイアログの背景をカスタム背景に設定
        dialog.setOnShowListener(d -> {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        });

        // ダイアログを表示
        dialog.show();
    }


    /**
     * 前の画面に戻る処理
     */
    public void ClickBack(View view) {
        // アクティビティを終了して前の画面に戻る
        finish();
    }

    /**
     * 設定画面に行く処理
     */
    public void ClickSet(View view) {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
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
