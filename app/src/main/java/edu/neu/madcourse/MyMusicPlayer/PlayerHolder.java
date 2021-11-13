package edu.neu.madcourse.MyMusicPlayer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerHolder extends RecyclerView.ViewHolder {
    public Context context;
    public Button btnPlay;
    public TextView tvTitle;
    public ImageView imgView;
    public PlayerHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        btnPlay = itemView.findViewById(R.id.btnPlay);
        tvTitle = itemView.findViewById(R.id.tv_title);
        imgView = itemView.findViewById(R.id.imgView);
    }
}
