package com.goenji.simplegallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
  //  private ScrollView scrollView;
  //  private static List<Image> imageList;
    private static List<Album> albumList;
    private String order = MediaStore.Images.Media.DATE_ADDED + " DESC ";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.gallery_album_list);
       // scrollView = findViewById(R.id.nested_scroll_view);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        sharedPreferences = getSharedPreferences("main_preferences", Context.MODE_PRIVATE);

        externalPermission();
    }

    private void listAllGalleryAlbum(String order) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.BUCKET_ID};
      //  imageList = new ArrayList<>();
        albumList = new ArrayList<>();
        try {
            Cursor c = getContentResolver().query(uri, projection, null, null, order);
            if(c != null) {
                if (c.moveToNext()) {
                    int imageId = c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
                    int data_added = c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED);
                    int imageTitle = c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                    //  int buckedTitle = c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
                    int buckedId = c.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID);

                    do {
                        long album_image_id = c.getLong(imageId);
                        long album_id = c.getLong(buckedId);
                        //String bucket_title = c.getString(buckedTitle);
                        String image_title = c.getString(imageTitle);
                        long album_data = c.getLong(data_added);

                        Uri albumUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, album_image_id);

                        Album album = new Album(album_id, image_title, album_data, albumUri, 0);
                        albumList.add(album);

                    } while (c.moveToNext());
                }
                c.close();
                setRecyclerView(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
            if (item.getItemId() == R.id.order_by_asc) {
                order = MediaStore.Images.Media.DISPLAY_NAME + " ASC ";
                sharedPreferences.edit().putString("saved_order", order).apply();
                listAllGalleryAlbum(order);
                return true;
            }

            if (item.getItemId() == R.id.order_by_desc) {
                order = MediaStore.Images.Media.DISPLAY_NAME + " DESC ";
                sharedPreferences.edit().putString("saved_order", order).apply();
                listAllGalleryAlbum(order);
                return true;
            }

            if (item.getItemId() == R.id.order_by_recent) {
                order = MediaStore.Images.Media.DATE_ADDED + " DESC ";
                sharedPreferences.edit().putString("saved_order", order).apply();
                listAllGalleryAlbum(order);
                return true;
            }

            if (item.getItemId() == R.id.order_by_older) {
                order = MediaStore.Images.Media.DATE_ADDED + " ASC ";
                sharedPreferences.edit().putString("saved_order", order).apply();
                listAllGalleryAlbum(order);
                return true;
            }

            if (item.getItemId() == R.id.about) {
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.DefaultAlertDialogStyle))
                        .setTitle("About")
                        .setMessage("Developer: Goenji48 - 2024")
                        .setPositiveButton("OK",
                                (dialog, which)
                                        -> dialog.cancel()).create();
                alertDialog.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void setRecyclerView(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        Adapter adapter = new Adapter(context, albumList);
        recyclerView.setAdapter(adapter);
    }

    private void externalPermission() {
        String[] permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                  String savedOrder = sharedPreferences.getString("saved_order", order);
                 listAllGalleryAlbum(savedOrder);
            } else {
                Toast.makeText(this, "Sua galeria de fotos não será exibida", Toast.LENGTH_SHORT).show();
            }
        });
        launcher.launch(permission[0]);
    }
}