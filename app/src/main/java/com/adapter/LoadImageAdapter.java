package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.notet.activity.R;

import java.util.ArrayList;

/**
 * Created by nguyenlinh on 01/03/2017.
 *
 * Load image demo note ,THIS NOT WORKING
 */
public class LoadImageAdapter  extends ArrayAdapter<String> {
    Activity context;
    int LayoutId;
    ArrayList<String> arrURL=null;
    ImageView imgView;
   // TextView mTxtTitle;

    ImageLoader imageLoader =ImageLoader.getInstance();

    public LoadImageAdapter(Activity context, int resource,ArrayList<String> arrURL) {
        super(context, resource,arrURL);
        this.context=context;
        this.LayoutId =resource;
        this.arrURL=arrURL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);
        if (arrURL.size()>0){
            imgView= (ImageView) convertView.findViewById(R.id.imgCustom);
            imageLoader.displayImage(arrURL.get(position),imgView);
        }
        return convertView;
    }
}
