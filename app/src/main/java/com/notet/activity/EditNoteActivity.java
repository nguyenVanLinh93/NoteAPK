package com.notet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.note.databasehandler.DabaseHandler;
import com.note.model.Notes;

public class EditNoteActivity extends AddNoteActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getControls() {
        super.getControls();
        loadData();
    }

    private void loadData() {
        // load Titie and content and image  Node edit
        mDabaseHandler = new DabaseHandler(this);
        Bundle bd = getIntent().getExtras();
        if (bd != null){
            int id = bd.getInt("id");
            Notes iNote = (Notes) mDabaseHandler.getNote(id);
            // set text
            mTxtTitle.setText(iNote.getTitle());
            mTxtContent.setText(iNote.getContent());
            //mLlAddNote.setBackgroundColor(Color.BLUE);
            // TODO set background
            mColor = Integer.parseInt(iNote.getBackground());
            //mLlAddNote.setBackgroundColor(mColor);
            int[] arrColor = {getResources().getColor(R.color.color_white), getResources().getColor(R.color.color_yellow),
                    getResources().getColor(R.color.color_green), getResources().getColor(R.color.color_blue)};
            switch (mColor) {
                case RESULT_COLOR_WHITE:
                    mLlAddNote.setBackgroundColor(arrColor[0]);
                    break;
                case RESULT_COLOR_YELLOW:
                    mLlAddNote.setBackgroundColor(arrColor[1]);
                    break;
                case RESULT_COLOR_GREEN:
                    mLlAddNote.setBackgroundColor(arrColor[2]);
                    break;
                case RESULT_COLOR_BLUE:
                    mLlAddNote.setBackgroundColor(arrColor[3]);
                    break;
                default:
                    mLlAddNote.setBackgroundColor(arrColor[0]);
                    break;
            }
            // set alarm
            if (!iNote.getAlarm().toString().equals(" ")){
                mLlAlarm.setEnabled(true);
                mLlAlarm.setVisibility(View.VISIBLE);
                TextView text = (TextView)findViewById(R.id.txtVgetAlarm);
                text.setText("the Note has set Alarm : "+iNote.getAlarm());
                Log.d("Visible linnear layout", "visible");
                spinerDropDown();
                mTxtAlarm.setVisibility(View.GONE);
                mStrAlarm = iNote.getAlarm();
                //Toast.makeText(getBaseContext(),iNote.getAlarm(),Toast.LENGTH_LONG).show();
                //mSpDate.setSelection(0);
                //mSpTime.setSelection(0);
            }
        }

        // load images
        //loadImages(); // TODO this
    }

    private void loadImages() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            int id = bd.getInt("id");
            Notes iNote = (Notes) mDabaseHandler.getNote(id);
            // get string bitmap
            // demo
            String s = iNote.getContent();
            Bitmap bmp = readBitmapFile(s);
            Log.d("insert photo","source "+bmp.toString());
            mGridImagesAdapter.addItem(bmp);
            mGridImagesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * create bitmap from string path
     * @param path
     * @return
     */
    public Bitmap readBitmapFile(String path){
        return BitmapFactory.decodeFile(path);
    }
    @Override
    public void addNote() {
        Bundle bd = getIntent().getExtras();
        int id = bd.getInt("id");
        Log.d("eidt mNote ", "id max " + id);
        insertNote(id);
    }

    public void deleteNote(){
        Bundle bd = getIntent().getExtras();
        int id = bd.getInt("id");
        mDabaseHandler.deleteNotes(id);
        Log.d("Edit mNote", "deleted node id = "+id);
        Intent i = new Intent(EditNoteActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void insert(Notes notes) {
        try {
            mDabaseHandler.updateNotes(notes);
            mDabaseHandler.close();
            Log.d("EditNoteActivity", "edit mNote success.." + mStrAlarm);

        } catch (Exception e) {
            Log.d("EditNoteActivity", "edit new a mNote errorr..." + e.toString());
        }
        // set alarm for this item.
        if (!mStrAlarm.toString().equals(" ")) {
            Log.d("EditNoteActivity", "edit mNote mStrAlarm.." + mStrAlarm);
            startAlert(notes.getID(), mStrAlarm);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eidt_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_insertphoto:
                InsertPhoto();
                break;
            case R.id.action_setbackground:
                SetBacground();
                break;
            case R.id.action_savenote:
                SaveNote();
                break;
            case R.id.action_deletenote:
                deleteNote();
                break;
            default:
                break;
        }
        // TODO create option menu share
        return true;
    }
}
