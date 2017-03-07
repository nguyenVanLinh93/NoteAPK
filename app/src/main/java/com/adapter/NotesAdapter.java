package com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.note.model.Notes;
import com.notet.activity.R;

import java.util.ArrayList;

/**
 * Created by nguyenlinh on 01/03/2017.
 */
public class NotesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Notes> arrNotes = new ArrayList<Notes>();

    public NotesAdapter(Context context, ArrayList<Notes> arrNotes) {
        super();
        this.context = context;
        this.arrNotes = arrNotes;
        Log.d("Load noteBaseAdapter","ddddddddddddddddddddd");
    }

    @Override
    public int getCount() {
        return arrNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return arrNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return arrNotes.indexOf(getItem(i));
    }

    private class ViewHolder {
        private TextView txtTitle;
        private TextView txtContent;
        private TextView txtCreateDate;
        private ImageView imgAlarm;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        Log.d("Note Adapter","note size :"+arrNotes.size());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            Log.d("Note Adapter","converView Null :");
            convertView = inflater.inflate(R.layout.custom_item_notes, null);
//            viewHolder.mTxtTitle = (TextView) convertView.findViewById(R.id.mTxtTitle);
//            viewHolder.mTxtContent = (TextView) convertView.findViewById(R.id.mTxtContent);
//            viewHolder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreatedDate);
//            viewHolder.imgAlarm = (ImageView) convertView.findViewById(R.id.imgAlarm);
             viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            Log.d("Note Adapter","converView Not Null :");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("Note Adapter","note size 2:"+arrNotes.size());
        Notes notes = arrNotes.get(i);

        viewHolder.txtTitle.setText(notes.getTitle());
        viewHolder.txtContent.setText(notes.getContent());
        viewHolder.txtCreateDate.setText(notes.getCreatedDate());

        if (notes.getAlarm() != null) {
            viewHolder.imgAlarm.setImageResource(R.drawable.ic_action_alarms);
        }

        return convertView;
    }
}
