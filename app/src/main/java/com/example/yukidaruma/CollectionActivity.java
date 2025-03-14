package com.example.yukidaruma;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionActivity extends AppCompatActivity {

    private FrameLayout collectionFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);


        collectionFrame = findViewById(R.id.collectionFrame);


    }

    /**
     * 画像をランダムな位置に配置するメソッド
     */
    private void addImageRandomly(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);

        // サイズを画像のサイズに基づいて設定
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        imageView.setLayoutParams(params);

        // FrameLayout の幅と高さを取得
        collectionFrame.post(() -> {
            int parentWidth = collectionFrame.getWidth();
            int parentHeight = collectionFrame.getHeight();

            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();

            // X座標を計算　全てはみ出さないように、経験則てきに500
            int marginX = 500;
            int x = ThreadLocalRandom.current().nextInt(parentWidth + marginX * 2 - imageWidth) - marginX;

            // Y座標を計算
            int marginY = 500;
            int y = ThreadLocalRandom.current().nextInt(parentHeight + marginY * 2 - imageHeight) - marginY;


            // 位置を設定
            imageView.setX(x);
            imageView.setY(y);

            // FrameLayout に追加
            collectionFrame.addView(imageView);
        });
    }

    /**
     * 保存した画像のリストを取得する
     */
    private List<Bitmap> getSavedSnowmanImages() {
        List<Bitmap> bitmapList = new ArrayList<>();

        // アプリ内保存ディレクトリを取得
        File dir = getFilesDir();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png")) {
                    // 画像を読み込む
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bitmap != null) {
                        bitmapList.add(bitmap); // リストに追加
                    }
                }
            }
        }

        return bitmapList; // 保存された画像のリストを返す
    }

    /**
     * 前の画面に戻る処理
     */
    public void ClickBack(View view) {
        // ホームに移動
        Intent homeIntent = new Intent(this, MainActivity.class); // HomeActivity は適切なアクティビティに変更
        startActivity(homeIntent);

    }
    /**
     * ボタンをクリックしたときにコレクション画面を開く
     */
    public void openAllCollectionActivity(android.view.View view) {
        Intent intent = new Intent(this, AllCollectionActivity.class);
        startActivity(intent);
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


        //画像を削除したときに反映されるようにするため
        if (collectionFrame != null) {
            // FrameLayout の子ビューをすべて削除
            collectionFrame.removeAllViews();

            // 保存された画像のリストを取得
            List<Bitmap> snowmanImages = getSavedSnowmanImages();

            // 画像をランダムな位置に再配置
            for (Bitmap bitmap : snowmanImages) {
                addImageRandomly(bitmap);
            }
        }
    }


}
