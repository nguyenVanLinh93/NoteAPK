package com.notet.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adapter.NotesArrayAdapter;
import com.note.databasehandler.DabaseHandler;
import com.note.model.Notes;

import java.util.ArrayList;


public class MainActivity extends Activity {

    Activity mActivity;
    DabaseHandler myDabaseHandler;


    ArrayList<Notes> mNotesArrayList = null;
    NotesArrayAdapter mAdapter = null;
    //NotesAdapter adapterNote=null;
    GridView mGvNotes;
    Notes mNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapDrawable background=new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.bg_actionbar));
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(background);// set background for action bar

        loadMain();

        // set on item click listener of gridview

    }
    // laoding data (use when call onCreate and onResume)
    public void loadMain() {

       myDabaseHandler = new DabaseHandler(this);
//        myDabaseHandler.deleteNotes();
//        myDabaseHandler.dropTable();
//        myDabaseHandler.CreateTable();
        //myDabaseHandler.deleteAllNotes();

        mNotesArrayList = myDabaseHandler.getAllNotesDESC();
        if (mNotesArrayList.size() > 0) {
            setContentView(R.layout.activity_main);
            mGvNotes = (GridView) findViewById(R.id.gvNote);
            Log.d("Main Notes size :", mNotesArrayList.size() + "");
            mAdapter = new NotesArrayAdapter(this, R.layout.custom_item_notes, mNotesArrayList);
            mGvNotes.setAdapter(mAdapter);

            // set on item click listener of gridview
            mGvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(MainActivity.this, EditNoteActivity.class);
                    Log.d("Move mActivity", "editor");
                    // pust extra
                    Notes iNote = mNotesArrayList.get(position);
                    // get id mNote and put to edit mActivity
                    it.putExtra("id", iNote.getID());
                    startActivity(it);
                    //Toast.makeText(getBaseContext(), "vi tri"+position, Toast.LENGTH_LONG).show();
                }
            });
            // set on item clong click listener TODO THIS : handler long clck listener
            /*mGvNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mGvNotes.setOnContextClickListener(this);
                    return false;
                }
            });*/
        } else {
            setContentView(R.layout.activity_main_no_notes);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMain();
        Log.d("MainActivity", "Call event OnResume()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mActivity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_addNote) {
            Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(i);
            Log.d("MainActivity", "Call AddNoteActivity class");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override TODO create function delete item when long click item
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setQwertyMode(true);
        MenuItem itemdelete = menu.add(0, 0, 0, "Delete");
        {
            itemdelete.setAlphabeticShortcut('a');
            itemdelete.setIcon(R.drawable.ic_action_discard);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(getBaseContext(),"delete item",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getBaseContext(),"no choise item",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }*/
}
