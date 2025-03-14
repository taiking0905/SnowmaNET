package com.example.yukidaruma;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllCollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_collection);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * 保存された画像を読み込む
     */
    private List<Bitmap> getSavedSnowmanImages() {
        List<Bitmap> images = new ArrayList<>();

        // アプリ内部ストレージのファイルリストを取得
        File directory = getFilesDir();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png")) { // PNG ファイルのみ読み込む
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        images.add(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // リストを逆順に並べ替える
        Collections.reverse(images);

        return images;
    }

    /**
     * RecyclerViewのデータをリセットして画像を再読み込む
     */
    private void reloadImages() {
        // 保存された画像を再読み込み
        List<Bitmap> images = getSavedSnowmanImages();

        // ファイル名リストを取得　日時を画像名からしゅとくからするため
        List<String> fileNames = new ArrayList<>();
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png")) { // PNG ファイルのみ対象
                    fileNames.add(file.getName()); // ファイル名をリストに追加
                }
            }
        }

        // アダプターが未設定の場合は新規作成
        if (adapter == null) {
            adapter = new ImageAdapter(images, fileNames);  // ファイル名も渡す
            recyclerView.setAdapter(adapter);
        } else {
            // 既存のデータをクリアして新しいデータをセット
            adapter.updateData(images, fileNames);  // 新しい画像とファイル名リストをセット
        }
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
        // 画像を再読み込み
        reloadImages();
    }
}
