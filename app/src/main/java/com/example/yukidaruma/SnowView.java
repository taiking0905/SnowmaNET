package com.example.yukidaruma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class SnowView extends View {

    private Paint snowPaint;
    private Snowflake snowflake;
    private Random random;
    private GestureDetector gestureDetector;

    private float growthRate; // 雪玉の成長速度

    public void updateGrowthRate(float newGrowthRate) {
        this.growthRate = newGrowthRate;
    }


    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);


        // 雪玉のペイントを設定
        snowPaint = new Paint();
        snowPaint.setStyle(Paint.Style.FILL);

        // ランダム生成器　場所をランダムにする
        random = new Random();

        // 雪玉を初期化（ランダムな位置とサイズ、色）
        snowflake = new Snowflake(
                random.nextInt(800),                // x座標
                random.nextInt(1600),               // y座標
                random.nextInt(10) + 5,             // サイズ
                generateRandomColor()               // 色
        );

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 雪玉の成長　再描画するたびに大きくなる
        snowflake.size += growthRate;

        // 雪玉の色を設定
        snowPaint.setColor(snowflake.color);

        // 雪玉を描画
        canvas.drawCircle(snowflake.x, snowflake.y, snowflake.size, snowPaint);
    }

    //雪玉クリックしたら
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //タッチされた位置
            float touchX = event.getX();
            float touchY = event.getY();

            // 雪玉の中心とタッチ位置の距離を計算
            float dx = touchX - snowflake.x;
            float dy = touchY - snowflake.y;
            //三平方を使って距離を求める
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // 距離が雪玉のサイズ以内ならクリックイベントを処理
            if (distance <= snowflake.size) {
                // 雪玉がクリックされたらスノーマンアクティビティへ遷移
                Context context = getContext();
                Intent intent = new Intent(context, SnowmanActivity.class);
                // Intent にデータを追加
                intent.putExtra("snowball_size", snowflake.size);
                intent.putExtra("snowball_color", snowflake.color);
                // アクティビティを開始
                context.startActivity(intent);
                return true;
            }


        }
        return super.onTouchEvent(event);
    }

    // ランダムな色を生成するメソッド　透けるようにして検索画面の邪魔にならないように
    private int generateRandomColor() {
        return Color.argb(0x88,random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // 雪玉を表すクラス
    private static class Snowflake {
        int x;      // x座標
        int y;      // y座標
        float size; // サイズ
        int color;  // 色

        Snowflake(int x, int y, float size, int color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
        }
    }

}
