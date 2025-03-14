package com.example.yukidaruma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SnowView snowView;
    private float growthRate = 0.5f; // 初期成長速度（デフォルト値）

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // レイアウト内の要素を取得
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView = findViewById(R.id.webView);
        snowView = findViewById(R.id.snowView);

        // WebView の設定
        webView.setWebViewClient(new WebViewClient());  // 外部ブラウザではなくアプリ内で表示
        webView.getSettings().setJavaScriptEnabled(true);  // JavaScriptを有効化
        webView.loadUrl("https://www.google.com");  // Google検索ページを表示

        // WebView のスクロールを検知　これで雪玉が大きくなる
        webView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int deltaY = scrollY - oldScrollY; // 縦方向のスクロール量
            if (deltaY != 0 && snowView != null) {
                // スクロール量に応じて成長速度を設定
                snowView.updateGrowthRate(Math.abs(deltaY) * growthRate);
                snowView.invalidate(); // ビューを再描画する
            }
        });

    }

    /**
     * ボタンをクリックしたときにコレクション画面を開く
     */
    public void openCollectionActivity(android.view.View view) {
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);
    }
    /**
     * 戻るボタンのクリック処理
     */
    public void ClickPageBack(View view) {
        if (webView.canGoBack()) {
            webView.goBack();  // 履歴があれば前のページに戻る
        }
    }

    /**
     * 設定画面に行く処理
     */
    public void ClickSet(View view) {
        Intent intent = new Intent(this, SetActivity.class);
        startActivity(intent);
    }

    //戻るボタンで戻ってきたとき、oncreateに行かないので、設定が変わったときに、情報を更新
    @Override
    protected void onResume() {
        super.onResume();
        // 背景色を再適用
        Utils.setBackgroundColor(this);
        // ステータスバーの色を変更
        Utils.setStatusBarColor(this);

        // ナビゲーションバーの色を変更
        Utils.setNavigationBarColor(this);

        // SharedPreferences から成長速度を取得
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        growthRate = sharedPreferences.getFloat("growthRate", 0.01f); // デフォルトは0.01f

        // SnowView の成長速度を更新
        if (snowView != null) {
            snowView.updateGrowthRate(growthRate);
        }
    }
}
