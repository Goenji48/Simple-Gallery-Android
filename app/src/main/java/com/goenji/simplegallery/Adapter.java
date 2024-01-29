package com.goenji.simplegallery;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Album> galleryList;

    public Adapter(Context context, List<Album> galleryList) {
        this.context = context;
        this.galleryList = galleryList;
        layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.card_view_album_list, parent, false);
        return new AdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Album albumItem = galleryList.get(position);
        holder.galleryAlbumTitle.setText(albumItem.getTitle());
       // holder.galleryAlbumDate.setText(formatDate(albumItem.getCreatedDate()));
        Glide.with(context).load(albumItem.getAlbumUri()).placeholder(R.drawable.image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.galleryAlbumImage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoViewActivity.class);
            intent.putExtra("IMAGE_URI", albumItem.getAlbumUri());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
    }

    private String formatDate(long data) {
        return DateFormat.format("MM/dd/yyyy", new Date(data)).toString();
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView galleryAlbumImage;
        private final TextView galleryAlbumTitle, galleryAlbumDate;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryAlbumImage = itemView.findViewById(R.id.gallery_album_image);
            galleryAlbumTitle = itemView.findViewById(R.id.gallery_album_title);
            galleryAlbumDate = itemView.findViewById(R.id.gallery_album_date);
        }
    }
}
