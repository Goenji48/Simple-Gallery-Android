package com.goenji.simplegallery;

import android.net.Uri;

public class Album {
    private long albumId;
    private long createdDate;
    private String title;
    private Uri albumUri;
    private long size;

    public Album(long albumId, String title, long createdData, Uri albumUri, long size) {
        this.albumId = albumId;
        this.title = title;
        this.createdDate = createdData;
        this.albumUri = albumUri;
        this.size = size;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.albumUri = albumUri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
