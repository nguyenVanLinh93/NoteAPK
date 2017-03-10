package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.note.model.Images;
import com.notet.activity.R;

import java.util.ArrayList;

/**
 * Created by nguyenlinh on 01/03/2017.
 *
 * Load images in activity
 */

public class GridImagesAdapter extends ArrayAdapter<Images> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Images> data = new ArrayList<>();

    public GridImagesAdapter(Context context, int layoutResourceId, ArrayList<Images> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Images item = data.get(position);
        holder.imageView.setImageBitmap(item.getImage());
        return row;
    }

    public void addItem(Bitmap image, String path) {
        data.add(new Images(image,path));
    }

    public void removeItem(int position){
        data.remove(position);
    }

    public String getPathItem(int position){
        return data.get(position).getPath();
    }

    public int sizeAdapter(){
        return data.size();
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
