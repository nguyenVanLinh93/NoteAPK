package com.note.model;

import android.graphics.Bitmap;

/**
 * Created by nguyenlinh on 01/03/2017.
 */

public class Images {
    private Bitmap image;

    public Images(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
