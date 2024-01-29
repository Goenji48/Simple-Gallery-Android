package com.goenji.simplegallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class PhotoViewActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private boolean isImageClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        toolbar = findViewById(R.id.photo_view_toolBar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView photoView = findViewById(R.id.photo_view_imageView);
        photoView.setImageURI(getImage());
        photoView.setOnClickListener(v -> {
            if(isImageClicked) {
                toolbar.setTranslationY(-9999);
                isImageClicked = false;
            } else {
                toolbar.setTranslationY(0);
                isImageClicked = true;
            }
        });
    }

    private Uri getImage() {
        Intent intent = getIntent();
        Parcelable parcelable = intent.getParcelableExtra(("IMAGE_URI"));
        if(parcelable != null) {
            return Uri.parse(parcelable.toString());
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.photo_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.btnShare) {
           shareImage();
           return true;
       } else {
           return super.onOptionsItemSelected(item);
       }
    }

    private void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Intent.EXTRA_TEXT,"Compartilhado de Simple Gallery");
        intent.putExtra(Intent.EXTRA_STREAM, getImage());
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Compartilhado de Simple Gallery"));
    }
}
