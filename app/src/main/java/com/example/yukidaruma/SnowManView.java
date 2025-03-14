package com.example.yukidaruma;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import java.io.IOException;
import java.io.OutputStream;

public class SnowManView extends View {

    private Paint snowPaint;
    private Snowflake snowflake;

    public SnowManView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 雪玉のペイントを設定
        snowPaint = new Paint();
        snowPaint.setStyle(Paint.Style.FILL);

        // 初期の雪玉を設定
        snowflake = new Snowflake(0, 0, 100.0, 0xFF0000FF); // デフォルト値
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 初回描画時に画面の中央を計算
        if (snowflake.x == 0 && snowflake.y == 0) {
            snowflake.x = getWidth() / 2.0;
            snowflake.y = getHeight() / 2.0;
        }



        /*雪だるまを描画するときに、sieなどを使って、小さくても大きくても描画がくずれないようにした*/

        // 雪だるまの色を設定　下　透ける防止　
        snowPaint.setColor(0xffffffff);
        // 胴体（下部の円）
        canvas.drawCircle((float) snowflake.x, (float) (snowflake.y + snowflake.size), (float) snowflake.size, snowPaint);

        // 雪だるまの色を設定
        snowPaint.setColor(snowflake.color);
        // 胴体（下部の円）
        canvas.drawCircle((float) snowflake.x, (float) (snowflake.y + snowflake.size), (float) snowflake.size, snowPaint);


        // 頭部（上部の円の設定）
        double headCenterX = snowflake.x;
        double headCenterY = snowflake.y - snowflake.size * 0.65;
        double headRadius = snowflake.size * 0.8;

        // 雪だるまの下地　上
        snowPaint.setColor(0xffffffff);
        // 胴体（上部の円）
        canvas.drawCircle((float) headCenterX, (float) headCenterY, (float) headRadius, snowPaint);

        // 雪だるまの色を設定上
        snowPaint.setColor(snowflake.color);
        // 胴体（上部の円）
        canvas.drawCircle((float) headCenterX, (float) headCenterY, (float) headRadius, snowPaint);

        // 目の色を設定
        Paint eyePaint = new Paint();
        eyePaint.setStyle(Paint.Style.FILL);
        eyePaint.setColor(0xFF000000); // 黒色

        // 左目
        float eyeOffsetX = (float) (headRadius * 0.4); // 頭部中心からの水平オフセット
        float eyeOffsetY = (float) (headRadius * 0.3); // 頭部中心からの垂直オフセット
        float eyeRadius = (float) (headRadius * 0.1);  // 目のサイズ

        canvas.drawCircle(
                (float) (headCenterX - eyeOffsetX),
                (float) (headCenterY - eyeOffsetY),
                eyeRadius,
                eyePaint
        );

        // 右目
        canvas.drawCircle(
                (float) (headCenterX + eyeOffsetX),
                (float) (headCenterY - eyeOffsetY),
                eyeRadius,
                eyePaint
        );

        // 鼻の色を設定（オレンジ色）
        Paint nosePaint = new Paint();
        nosePaint.setStyle(Paint.Style.FILL);
        nosePaint.setColor(0xFFFF7A00); // 明るいオレンジ

        // 鼻（三角形）の描画
        float noseBaseWidth = (float) (headRadius * 0.25); // 鼻の幅
        float noseHeight = (float) (headRadius * 0.25);    // 鼻の高さ

        float noseTipX = (float) headCenterX;
        float noseTipY = (float) headCenterY; // 頭部の中心
        float noseLeftX = noseTipX - noseBaseWidth / 2;
        float noseLeftY = noseTipY + noseHeight;
        float noseRightX = noseTipX + noseBaseWidth / 2;
        float noseRightY = noseTipY + noseHeight;

        Path nosePath = new Path();
        nosePath.moveTo(noseTipX, noseTipY);      // 頂点
        nosePath.lineTo(noseLeftX, noseLeftY);    // 左下
        nosePath.lineTo(noseRightX, noseRightY);  // 右下
        nosePath.close();                         // 三角形を閉じる

        canvas.drawPath(nosePath, nosePaint);

        // 口の色を設定（黒色）
        Paint mouthPaint = new Paint();
        mouthPaint.setStyle(Paint.Style.STROKE);
        mouthPaint.setColor(0xFF000000); // 黒色
        mouthPaint.setStrokeWidth(10.0f); // 線の太さ

        // 口（円弧）の描画
        float mouthRadius = (float) (headRadius * 0.6); // 口の半径
        float mouthCenterY = (float) (headCenterY + headRadius * 0.1); // 頭部中心より少し下
        canvas.drawArc(
                (float) (headCenterX - mouthRadius), // 左上のx
                (float) (mouthCenterY - mouthRadius), // 左上のy
                (float) (headCenterX + mouthRadius), // 右下のx
                (float) (mouthCenterY + mouthRadius), // 右下のy
                30,  // 開始角度（30度）
                120, // スイープ角度（120度）
                false, // 線を閉じない
                mouthPaint
        );

        // ボタンの色を設定
        Paint buttonPaint = new Paint();
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setColor(0xFF000000); // 黒色

        // ボタンの描画
        float buttonRadius = (float) (snowflake.size * 0.1);
        float buttonOffsetY = (float) (snowflake.size * 0.4);
        for (int i = -1; i <= 1; i++) {
            canvas.drawCircle(
                    (float) snowflake.x,
                    (float) (snowflake.y + snowflake.size + buttonOffsetY * i),
                    buttonRadius,
                    buttonPaint
            );
        }
    }

    // 雪玉のサイズと色を設定するメソッド
    public void setSnowflakeAttributes(double size, int color) {
        snowflake.size = size;
        snowflake.color = color;

        // 描画をリクエスト
        invalidate();
    }

    // 雪玉を表すクラス
    private static class Snowflake {
        double x;      // x座標
        double y;      // y座標
        double size;   // サイズ
        int color;     // 色

        Snowflake(double x, double y, double size, int color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
        }
    }
    //保存するためにビットマップにする
    public Bitmap getSnowmanBitmap() {
        // 透明背景のビットマップを作成
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 背景を透明に設定
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

        // ビットマップにSnowManViewを描画
        draw(canvas);
        return bitmap;
    }

}
