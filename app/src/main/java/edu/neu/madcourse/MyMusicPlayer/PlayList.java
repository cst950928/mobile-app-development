package edu.neu.madcourse.MyMusicPlayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayList extends AppCompatActivity {
    private static final String TAG = "PlayList";
    private Button btnList;
    private ArrayList<edu.neu.madcourse.MyMusicPlayer.MusicItem> items = new ArrayList<>();
    private TextView tvFileName;
    static MediaPlayer mediaPlayer;
    private static final String FILE_PATH = "/sdcard/Download";
    private String filePath;
    private RecyclerView recyclerView;
    private PlayerAdaptor adaptor;
    private ArrayList<edu.neu.madcourse.MyMusicPlayer.MusicItem> localItems = new ArrayList<>();
    private boolean isVisible = true, initialList = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        btnList = findViewById(R.id.btnList);
        tvFileName = findViewById(R.id.tvFileName);


        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialList) {
                    runtimePermission();
                    initialList = false;
                    btnList.setText("Try New Songs");
                }
                else {
                    if (isVisible) {
                        recyclerView.setVisibility(View.GONE);
                        isVisible = false;
                        btnList.setText("Display My List");
                    }
                    else{
                        recyclerView.setVisibility(View.VISIBLE);
                        isVisible = true;
                        btnList.setText("Try New Songs");
                    }

                }
            }
        });


        tvFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    filePath = new File(FILE_PATH) + "/" + items.get(0).getName();
                    prepareMediaPlayer(filePath);
                }
                else if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                else {
                    mediaPlayer.start();
                }
            }
        });
    }

    public void prepareMediaPlayer(String filePath) {
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PlayList.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
    public void runtimePermission() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(PlayList.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                displaySongs();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        }
        else
        {
            displaySongs();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displaySongs();
            }
        }
    }

    public void createRecyclerView() {
        recyclerView = findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        adaptor = new PlayerAdaptor(PlayList.this, localItems);
        adaptor.notifyDataSetChanged();
//        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItem(int position, File file) {
        localItems.add(position, new LocalItem(file));
        adaptor.notifyItemInserted(position);
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arr = new ArrayList<>();
        File[] f = file.listFiles();
//        Toast.makeText(PlayList.this, file.toString(), Toast.LENGTH_LONG).show();
        for (File singleFile: f) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arr.addAll(findSong(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    arr.add(singleFile);
                }
            }
        }
        return arr;
    }

    public void displaySongs() {
//        Environment.getExternalStorageDirectory()
        File file = new File(FILE_PATH);
        final ArrayList<File> mySongs = findSong(file);
        Toast.makeText(PlayList.this, String.valueOf(mySongs.size()), Toast.LENGTH_LONG).show();
//        items = new File[mySongs.size()];
        createRecyclerView();
        for (int i = 0; i < mySongs.size(); i++) {
//            items[i] = mySongs.get(i);
            addItem(0, mySongs.get(i));
            items.add(0, new LocalItem(mySongs.get(i)));
            Log.d(TAG, "add new song");
        }
        recyclerView.setAdapter(adaptor);
//        if (playItems.size() > 0)  {
////            tvFileName.setText(items[0].getName().toString());
//            Toast.makeText(PlayList.this, "Current file: " + playItems.get(0).getFile().getName(), Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(PlayList.this, "No mp3 files", Toast.LENGTH_LONG).show();
//        }
    }
}