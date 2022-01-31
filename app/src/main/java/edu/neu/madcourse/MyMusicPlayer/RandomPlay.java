package edu.neu.madcourse.MyMusicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

/**
 * On contrary with local files, RamdomPlay will get mp3 files in Firebase Storage
 */
public class RandomPlay extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer;
    private Button btnStart;
    private RecyclerView recyclerView;
    private StoragePlayerAdaptor adaptor;
    private ArrayList<OnlineItem> localItems;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_play);
        btnStart = findViewById(R.id.btnStart);
        localItems = new ArrayList<>();

        mediaPlayer = new MediaPlayer();
        btnStart.setOnClickListener(this::onClick);
        createRecyclerView();
//        prepareMediaPlayer();
    }

    /**
     * Initialize recyclerview, including binding variable with view
     * Set the adaptor and layout manager for the recyclerview
     * Set the data for recyclerview
     */
    public void createRecyclerView() {
        recyclerView = findViewById(R.id.recViewRandom);
        recyclerView.setHasFixedSize(true);
        adaptor = new StoragePlayerAdaptor(RandomPlay.this, localItems);
        adaptor.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        Get Files on Firebase Storage
         */
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference fileRef: listResult.getItems()) {
                    fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            String filename = storageMetadata.getName(); //get the filename of file in Storage

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) { //call back: get filename and generate mp3 object
                                    OnlineItem onlineItem = new OnlineItem(filename, uri.toString());
                                    localItems.add(onlineItem);
                                    recyclerView.setAdapter(adaptor);
                                }
                            });
                        }
                    }).addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            ;
                        }
                    });
                }

            }
        });
//
//        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//                for (StorageReference fileRef: listResult.getItems()) {
//                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            localItems.add(uri.toString());
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            recyclerView.setAdapter(adaptor);
//                        }
//                    });
//                }
//            }
//        });
        
    }
//    public void addItem(int position, String item) {
//        localItems.add(0, item);
//        adaptor.notifyItemInserted(position);
//    }
    public void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/mymusicplayer-19ec0.appspot.com/o/Jay%20Chou%20Excuse.mp3?alt=media&token=0c7b5d77-ba4d-4f61-86d7-dbe132aab858");
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(RandomPlay.this, "Error", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.pause();
//                }
//                else{
//                    mediaPlayer.start();
//                }
        }
    }
}
