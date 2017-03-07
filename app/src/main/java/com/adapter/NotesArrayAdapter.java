package com.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.note.model.Notes;
import com.notet.activity.AddNoteActivity;
import com.notet.activity.R;

import java.util.ArrayList;

/**
 * Created by nguyenlinh on 24/02/2017.
 *
 * class use to custom item view for gridview
 */
public class NotesArrayAdapter extends ArrayAdapter<Notes> {

    // declare avariable use to call this contructor
    Activity context;
    ArrayList<Notes> arrNotes = null;
    int LayoutId;

    // all controls
    TextView txtTitle, txtContent, txtCreatedDate;
    ImageView imgAlarm;


    // function contructor of ArrayAdapter
    public NotesArrayAdapter(Activity context, int resource, ArrayList<Notes> arrNotes) {
        super(context, resource, arrNotes);
        this.context = context;
        this.arrNotes = arrNotes;
        this.LayoutId = resource;
        Log.d("NotesArray Adapter", "calling contrustor..");

    }

    // function use to custom item view
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);

        Log.d("NotesArray Adapter", "load controls...");
        if (arrNotes.size() > 0) {
            Log.d("NotesArray Adapter", "arrNotes size:" + arrNotes.size());
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitleCustom);
            txtContent = (TextView) convertView.findViewById(R.id.txtContentCustom);
            txtCreatedDate = (TextView) convertView.findViewById(R.id.txtCreatedDateCustom);
            imgAlarm = (ImageView) convertView.findViewById(R.id.imgAlarmCustom);


            Notes notes = arrNotes.get(position);
            Log.d("NotesArray Adapter", "load background..." + notes.getBackground());
            Log.d("NotesArray Adapter", "load alarm..." + notes.getAlarm());
            Log.d("NotesArray Adapter", "load id..." + notes.getId());
            txtTitle.setText(notes.getTitle());
            txtContent.setText(notes.getContent());
            txtCreatedDate.setText(notes.getCreatedDate());

            // setbackgound for item view
            int color = Integer.parseInt(notes.getBackground().trim());
            if (color == AddNoteActivity.RESULT_COLOR_BLUE) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_blue));
            } else if (color == AddNoteActivity.RESULT_COLOR_YELLOW) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_yellow));

            } else if (color == AddNoteActivity.RESULT_COLOR_GREEN) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_green));
            }
            //.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_alarms2));
            // Set imgAlarm resources
            if (!notes.getAlarm().equals(" ")) {
                Log.d("Notes arr adapter", "load img..." + notes.getAlarm());
                imgAlarm.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_alarms));
                // imgAlarm.setImageResource(R.drawable.ic_action_alarms);
            }
        }

        return convertView;

    }
}
