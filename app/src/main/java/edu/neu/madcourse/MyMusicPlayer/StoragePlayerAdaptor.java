package edu.neu.madcourse.MyMusicPlayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoragePlayerAdaptor extends RecyclerView.Adapter<PlayerHolder> {
    private Context context;
    private ArrayList<OnlineItem> items;

    public StoragePlayerAdaptor(Context context, ArrayList<OnlineItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PlayerHolder holder = new PlayerHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        OnlineItem item = items.get(position);
        holder.tvTitle.setText(item.getName());
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPlay = new Intent(context, Player.class).putExtra("SongList", items)
                        .putExtra("Name", item.getName())
                        .putExtra("url", item.getUrl())
                        .putExtra("pos", position)
                        .putExtra("src", "FireBase");
                context.startActivity(intentPlay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
