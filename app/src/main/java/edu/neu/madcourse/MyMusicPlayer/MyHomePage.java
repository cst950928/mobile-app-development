package edu.neu.madcourse.MyMusicPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyHomePage extends AppCompatActivity implements View.OnClickListener{
    private Button btnAdd, btnPlayList, btnRandomPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home_page);
        btnAdd = findViewById(R.id.btnAddLabel);
        btnRandomPlay = findViewById(R.id.btnRandomPlay);
        btnPlayList = findViewById(R.id.btnPlayList);
        btnAdd.setOnClickListener(this::onClick);
        btnPlayList.setOnClickListener(this::onClick);
        btnRandomPlay.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btnAddLabel:
//                Intent intentAdd = new Intent(Homepage.this, AddMusic.class);
//                startActivity(intentAdd);
//                break;
            case R.id.btnPlayList:
                Intent intentPlayList = new Intent(MyHomePage.this, PlayList.class);
                startActivity(intentPlayList);
                break;
            case R.id.btnRandomPlay:
                Intent intentRandom = new Intent(MyHomePage.this, RandomPlay.class);
                startActivity(intentRandom);
            default:
                break;
        }
    }
}