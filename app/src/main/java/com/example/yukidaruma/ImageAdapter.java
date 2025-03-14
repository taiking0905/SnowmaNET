package com.example.yukidaruma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<Bitmap> images;
    private final List<String> fileNames;

    public ImageAdapter(List<Bitmap> images, List<String> fileNames) {
        this.images = images; // 画像をリストで受け取る
        this.fileNames = fileNames; // ファイル名をリストで受け取る
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);//画像の表示xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(images.get(position));

        // ファイル名から日時部分を抽出
        String fileName = fileNames.get(position);
        String timestampString = fileName.replace("snowman_", "").replace(".png", "");

        // 表示用に整形（例: "20250122_221847" → "2025/01/22 22:18:47"）
        String formattedDate = formatTimestampString(timestampString);

        // 保存日時をテキストビューに表示
        holder.dateTextView.setText("保存日時: " + formattedDate);
    }

    //画像の数によって、viewの数を作成する
    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    public void updateData(List<Bitmap> newImages, List<String> newFileNames) {
        images.clear();
        images.addAll(newImages);
        fileNames.clear();
        fileNames.addAll(newFileNames);
        notifyDataSetChanged();
    }

    /**
     * ファイル名からタイムスタンプを抽出する
     * 例: snowman_20250122_221847.png -> 2025年1月22日 22時18分47秒
     */
// "yyyyMMdd_HHmmss" の形式を "yyyy/MM/dd HH:mm:ss" に整形するメソッド
    private String formatTimestampString(String timestampString) {
        return timestampString.substring(0, 4) + "/" +
                timestampString.substring(4, 6) + "/" +
                timestampString.substring(6, 8) + " " +
                timestampString.substring(9, 11) + ":" +
                timestampString.substring(11, 13) + ":" +
                timestampString.substring(13);
    }
}
