package edu.neu.madcourse.MyMusicPlayer;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    private boolean isFireBase = false;
    private Button btnStart, btnNext, btnPrev;
    static MediaPlayer mediaPlayer;
    private ArrayList<MusicItem> items;
    private String songName;
    private TextView tvchange, tvSongName, tvHint, tvStart, tvStop;
    private BarVisualizer visualizer;
    private ImageView imgPlay;
    private int pos;
    private static final String FILE_PATH = "/sdcard/Download";
    private static final String TAG = "Player";
    private String filePath;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer; //accelerometer in phone
    private boolean firstUpdate = true;
    private boolean initialShake = false;
    private SeekBar seekBar;
    private double accelerationCurrentValueX, accelerationPrevValueX, accelerationCurrentValueY, accelerationPrevValueY, accelerationCurrentValueZ, accelerationPrevValueZ;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            updateAccelParams(x, y, z);
            int id = isAccelerationChange();
            int n = items.size();
            if (!initialShake && id > 0) {
                initialShake = true;
            }
            else if (initialShake && id > 0) {
                shakeAction(id, n);
                Log.d(TAG, "Shaked!!!");
                initialShake = false;
            }
            else if (initialShake && id == 0) {
                initialShake = false;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    Thread updateSeekbar;
    private void shakeAction(int id, int n) {
        if (id == 1) {
            pos = (pos - 1 + n) % n;
            tvHint.setText("prev");
        }
        else {
            pos = (pos + 1) % n;
            tvHint.setText("next");
        }

        mediaPlayer.stop();
        mediaPlayer.release();
        if (isFireBase) {
            filePath = items.get(pos).getUrl();
        }
        else {
            filePath = new File(FILE_PATH) + "/" + items.get(pos).getName();
        }
        mediaPlayer = new MediaPlayer();
        songName = items.get(pos).getName();
        prepareMediaPlayer(filePath);

    }
    private int isAccelerationChange() {
        double changeAccelerationX = accelerationCurrentValueX - accelerationPrevValueX;
        double changeAccelerationY = accelerationCurrentValueY - accelerationPrevValueY;
        double changeAccelerationZ = accelerationCurrentValueZ - accelerationPrevValueZ;
        tvchange.setText(String.valueOf(changeAccelerationX));

        double xy = changeAccelerationX * changeAccelerationX + changeAccelerationY * changeAccelerationY, yz = changeAccelerationZ * changeAccelerationZ + changeAccelerationY * changeAccelerationY;
        if (xy > 20 || yz > 20) {
            if (xy > yz) {
                return 1;
            }
            else return 2;
        }
        return 0;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnStart = findViewById(R.id.btnStart);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        seekBar = findViewById(R.id.seekBar);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tvchange = findViewById(R.id.tv_change);
        tvSongName = findViewById(R.id.tv_songName);
        tvHint = findViewById(R.id.tv_hint);
        tvStart = findViewById(R.id.tvStart);
        tvStop = findViewById(R.id.tvStop);
        visualizer = findViewById(R.id.blast);
//        imgPlay = findViewById(R.id.imgPlay);

        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intentPlay = getIntent();
        Bundle bundle = intentPlay.getExtras();

        if (bundle.containsKey("src") && bundle.getString("src").equals("FireBase")) {
            isFireBase = true;
        }
        if (isFireBase) {
            filePath = bundle.getString("url");
            items = (ArrayList)bundle.getParcelableArrayList("SongList");
        }
        else {
            items = (ArrayList)bundle.getParcelableArrayList("SongList");
            filePath = new File(FILE_PATH) + "/" + items.get(pos).getName();
        }
        songName = bundle.getString("Name");
        pos = bundle.getInt("pos");
        mediaPlayer = new MediaPlayer();
        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPos = 0;
                while (currentPos < totalDuration) {
                    try{
                        sleep(500);
                        currentPos = mediaPlayer.getCurrentPosition();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        });

                    }
                    catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        prepareMediaPlayer(filePath);
        runtimePermission();

        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String curTime = getTime(mediaPlayer.getCurrentPosition());
                tvStart.setText(curTime);
                handler.postDelayed(this, delay);
            }
        }, delay);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnStart.setBackgroundResource(R.drawable.ic_play);
                }
                else {
                    mediaPlayer.start();
                    btnStart.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                pos = (pos + 1) % items.size();
                if (isFireBase) {
                    filePath = items.get(pos).getUrl();
                }
                else {
                    filePath = new File(FILE_PATH) + "/" + items.get(pos).getName();
                }
                mediaPlayer = new MediaPlayer();
                songName = items.get(pos).getName();
                btnStart.setBackgroundResource(R.drawable.ic_pause);
                prepareMediaPlayer(filePath);
//                startAnimation(imgPlay);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();
                int n = items.size();
                pos = (pos - 1 + n) % n;
                if (isFireBase) {
                    filePath = items.get(pos).getUrl();
                }
                else {
                    filePath = new File(FILE_PATH) + "/" + items.get(pos).getName();
                }
                mediaPlayer = new MediaPlayer();
                songName = items.get(pos).getName();
                btnStart.setBackgroundResource(R.drawable.ic_pause);
//                startAnimation(imgPlay);
                prepareMediaPlayer(filePath);

            }
        });


    }

    public void runtimePermission() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(Player.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                int id = mediaPlayer.getAudioSessionId();
                Log.d(TAG, String.valueOf(id));
                if (id != -1) {
                    visualizer.setAudioSessionId(0);
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            }
        }
        else
        {
            int id = mediaPlayer.getAudioSessionId();
            Log.d(TAG, String.valueOf(id));
            if (id != -1) {
                visualizer.setAudioSessionId(0);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                int id = mediaPlayer.getAudioSessionId();
                Log.d(TAG, String.valueOf(id));
                if (id != -1) {
                    visualizer.setAudioSessionId(0);
                }
            }
        }
    }

    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
    private String getTime(int currentPosition) {
        String res = "";
        int minute = currentPosition / 1000 / 60;
        int sec = currentPosition / 1000 % 60;
        Log.d(TAG, String.valueOf(currentPosition) + "," + String.valueOf(sec));
        res += minute + ":";
        if (sec < 10) {
            res += "0";
        }
        res += sec;
        return res;
    }

    public void prepareMediaPlayer(String filePath) {
        try {
            tvSongName.setText(songName);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    String endTime = getTime(mediaPlayer.getDuration());
                    tvStop.setText(endTime);
                    prepareSeekBar();
                    startAnimation(imgPlay);
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Player.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
    public void prepareSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        if (!updateSeekbar.isAlive()) updateSeekbar.start();
    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
        if (visualizer != null) {
            visualizer.hide();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (visualizer != null) {
            visualizer.release();
        }
    }

    private void updateAccelParams(float xNewAccel, float yNewAccel, float zNewAccel) {
        if (firstUpdate) {
            firstUpdate = false;
            accelerationPrevValueX = xNewAccel;
            accelerationPrevValueY = yNewAccel;
            accelerationPrevValueZ = zNewAccel;
        }
        else {
            accelerationPrevValueX = accelerationCurrentValueX;
            accelerationPrevValueY = accelerationCurrentValueY;
            accelerationPrevValueZ = accelerationCurrentValueZ;
        }
        accelerationCurrentValueX = xNewAccel;
        accelerationCurrentValueY = yNewAccel;
        accelerationCurrentValueZ = zNewAccel;
    }
}