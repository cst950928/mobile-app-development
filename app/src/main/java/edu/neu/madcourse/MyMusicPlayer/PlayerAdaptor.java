package edu.neu.madcourse.MyMusicPlayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

/**
 * Adaptor for recyclerview
 */
public class PlayerAdaptor extends RecyclerView.Adapter<PlayerHolder>{
    private Context context;
    private ArrayList<MusicItem> localItems;
//    private ArrayList<File> items;
    public PlayerAdaptor(Context context, ArrayList<MusicItem> localItems) {
        this.context = context;
        this.localItems = localItems;
//        this.items = items;
    }

    /**
     * Inflate layout for each item in recyclerview
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PlayerHolder holder = new PlayerHolder(view, context);
        return holder;
    }

    /**
     * Bind the position and data with each item in recyclerview
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        MusicItem item = localItems.get(position);
        holder.tvTitle.setText(item.getFile().getName());
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPlay = new Intent(context, Player.class).putExtra("SongList", localItems)
                        .putExtra("Name", item.getFile().getName())
                        .putExtra("pos", position);
                context.startActivity(intentPlay);
            }
        });
    }

    /**
     * Get the number of items in current list
     * @return
     */
    @Override
    public int getItemCount() {
        return localItems.size();
    }

}
