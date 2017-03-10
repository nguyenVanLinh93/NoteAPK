package com.note.model;

import android.graphics.Bitmap;

/**
 * Created by nguyenlinh on 01/03/2017.
 */

public class Images {
    private Bitmap image;
    private String path;

    public Images(Bitmap image, String path) {
        this.image = image;
        this.path = path;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
