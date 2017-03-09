package com.notet.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adapter.GridImagesAdapter;
import com.com.note.broadcastreceiver.NoteBroadcastReceiver;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.note.databasehandler.DabaseHandler;
import com.note.model.Images;
import com.note.model.Notes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddNoteActivity extends Activity {

    // request and result code
    public static final int REQUEST_CODE_ADD_NOTE = 111;
    public static final int RESULT_COLOR = 113;
    public static final int RESULT_PHOTO = 115;
    public static final int REQUEST_TAKE_PHOTO = 0;
    //  mColor use to setbackground for this and Notes
    public static final int RESULT_COLOR_WHITE = 200;
    public static final int RESULT_COLOR_YELLOW = 201;
    public static final int RESULT_COLOR_GREEN = 202;
    public static final int RESULT_COLOR_BLUE = 203;
    // db
    protected DabaseHandler mDabaseHandler;
    protected Notes mNotes;
    // all controls
    protected EditText mTxtTitle, mTxtContent;
    protected ImageView mBtnCancel;
    protected TextView mTxtCurrentDate, mTxtAlarm;
    protected Spinner mSpDate, mSpTime;
    protected LinearLayout mLlAddNote, mLlAlarm;

    // dialog
    protected static final int TIME_DIALOG_ID = 155;
    protected static final int DATE_DIALOG_ID = 166;
    // time and date


    // arr and mAdapter for spiner
    protected ArrayAdapter<String> mAdapterDay = null;
    protected ArrayAdapter<String> mAdapterTime = null;
    protected String mArrDate[] = null;
    protected String mArrTime[] = null;

    // time
    protected Calendar mCalendar = Calendar.getInstance();
    protected static int Hour, Min, Day, Month, Year;
    protected int mColor;
    protected String mCurrentDateTime = "";
    protected String mStrAlarm = " ";
    protected String mSStrDay = "";
    protected String mSStrTime = "";

    // image mAdapter
    protected GridView mGridView;
    protected GridImagesAdapter mGridImagesAdapter;
    protected ArrayList<Images> dataImage = new ArrayList<>();
    // string images
    protected ArrayList<Bitmap> mBitmapArrayList = new ArrayList<>();
    protected String mString = "";
    protected String mCurrentPhotoPath="";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    protected GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.bg_actionbar));

        ActionBar actionBar = getActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));
        actionBar.setBackgroundDrawable(background);

        mArrDate = getResources().getStringArray(R.array.arrdate);
        mArrTime = getResources().getStringArray(R.array.arrtime);
        getControls();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getControls() {

        mSpDate = (Spinner) findViewById(R.id.spDate);
        mSpTime = (Spinner) findViewById(R.id.spTime);
        mTxtContent = (EditText) findViewById(R.id.txtAddContent);
        mTxtTitle = (EditText) findViewById(R.id.txtAddTitle);
        mTxtAlarm = (TextView) findViewById(R.id.txtAlarm);
        mTxtCurrentDate = (TextView) findViewById(R.id.txtCreatedDate);
        mBtnCancel = (ImageView) findViewById(R.id.btnCancel);
        mLlAddNote = (LinearLayout) findViewById(R.id.llAddNote);
        mLlAlarm = (LinearLayout) findViewById(R.id.llAlarm);
        mLlAlarm.setEnabled(false);
        mGridView = (GridView) findViewById(R.id.grid_image);
        mGridImagesAdapter = new GridImagesAdapter(this, R.layout.grid_item_layout, dataImage);
        // set mAdapter Image
        mGridView.setAdapter(mGridImagesAdapter);
        //
        mCalendar = Calendar.getInstance();
        mCurrentDateTime = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + mCalendar.get(Calendar.MONTH) + "/" + mCalendar.get(Calendar.YEAR) + " "
                + mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE);
        mTxtCurrentDate.setText(mCurrentDateTime);

        // show spinner
        mTxtAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLlAlarm.setEnabled(true);
                mLlAlarm.setVisibility(View.VISIBLE);
                Log.d("Visible linnear layout", "visible");
                spinerDropDown();
                mTxtAlarm.setVisibility(View.GONE);
            }
        });
        // hide spinner
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLlAlarm.setVisibility(LinearLayout.INVISIBLE);
                mLlAlarm.setEnabled(false);
                mTxtAlarm.setVisibility(View.VISIBLE);
                mSStrTime = "";
                mSStrDay = "";
                mStrAlarm = " ";
            }
        });
    }

    // show data spinner
    public void spinerDropDown() {
        try {
            mAdapterDay = new ArrayAdapter<String>(AddNoteActivity.this, android.R.layout.simple_spinner_item, mArrDate);
            mAdapterTime = new ArrayAdapter<String>(AddNoteActivity.this, android.R.layout.simple_spinner_item, mArrTime);

            mAdapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpDate.setAdapter(mAdapterDay);
            mSpTime.setAdapter(mAdapterTime);

            mSpDate.setOnItemSelectedListener(new myOnItemClickListener());
            mSpTime.setOnItemSelectedListener(new myOnItemClickListener2());

        } catch (Exception e) {
            Log.d("AddNoteActivity", "Load data for spinner Error...");
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddNote Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    private class myOnItemClickListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 0:
                    mSStrDay = setDate(0);
                    break;
                case 1:
                    mSStrDay = setDate(1);
                    break;
                case 2:
                    mSStrDay = setDate(2);
                    break;
                case 3:
                    showDialog(DATE_DIALOG_ID);
                    mSStrDay = Day + "/" + Month + "/" + Year;
                    mArrDate[3] = mSStrDay;
                    break;
                default:
                    mSStrDay = "";
                    break;
            }
            mAdapterDay.notifyDataSetChanged();
            Log.d("adapterday ", mSStrDay);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mStrAlarm = " ";
        }
    }

    // set date when spinner seleted item
    public String setDate(int i) {
        mCalendar.add(Calendar.DATE, i);
        Day = mCalendar.get(Calendar.DAY_OF_MONTH);
        Month = mCalendar.get(Calendar.MONTH);
        Year = mCalendar.get(Calendar.YEAR);

        return Day + "/" + Month + "/" + Year;
    }

    private class myOnItemClickListener2 implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                    mSStrTime = mSpTime.getSelectedItem().toString();
                    break;
                default:
                    mSStrTime = "";
                    break;
            }
            mAdapterTime.notifyDataSetChanged();
            Log.d("adapterday ", mSStrTime);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mStrAlarm = " ";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mActivity in AndroidManifest.xml.
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // call insertphoto mActivity
    public void InsertPhoto() {
        //Intent i = new Intent(AddNoteActivity.this, InsertPhotosActivity.class);
        //startActivityForResult(i, REQUEST_CODE_ADD_NOTE);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
        builder.setTitle("Insert Picture");
        final CharSequence[] items = {"Take Photo", "Choose Photo"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        cameraIntent();
                        //dispatchTakePictureIntent(); // demo save file
                        break;
                    case 1:
                        galleryIntent();
                        break;
                }
            }
        });
        builder.create().show();
        Log.d("AddNoteActivity", "Call mActivity InertPhoto");
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    // call choosecolor mActivity
    public void SetBacground() {
        Intent i = new Intent(AddNoteActivity.this, ChooseColorActivity.class);
        startActivityForResult(i, REQUEST_CODE_ADD_NOTE);
        Log.d("AddNoteActivity", "Call mActivity ChooseColorActivity");
    }

    // save a new mNotes
    public void SaveNote() {

        mStrAlarm = mSStrDay.toString() +" "+ mSStrTime.toString();
        Log.d("AddNoteActivity", "nndndnnn" + mSStrDay.toString());
        Log.d("AddNoteActivity", "nndndnnn" + mSStrTime.toString());

        if (mTxtTitle.getText().toString().equals("")) {
            if (mTxtContent.getText().toString().equals("")) {
                if (mStrAlarm.toString().equals(" ")) {
                    finish();
                    Log.d("all null", "cancel insert");
                } else {
                    mTxtTitle.setText("Untitle");
                    addNote();
                    finish();
                    Log.d("mStrAlarm not null", "insert Untitle");
                }
            } else {
                mTxtTitle.setText(mTxtContent.getText().toString());
                addNote();
                finish();
                Log.d("Title null", "insert no title");
            }
        } else {
            addNote();
            finish();
            Log.d("title not null", "insert mNote");
        }
    }

    public void addNote() {
        mDabaseHandler = new DabaseHandler(this);
        int id = mDabaseHandler.idMax() + 1; // set id ex: if max id = 2 -> id next = 3
        Log.d("add mNote ", "id max " + id);
        insertNote(id); // put id
    }

    public void insertNote(int id) {
        mNotes = new Notes();
        mNotes.setID(id);
        mNotes.setTitle(mTxtTitle.getText() + "");
        //mNotes.setContent(mTxtContent.getText() + "");
        mNotes.setContent(mString); // demo save image
        Log.d("AddNote Created date", mTxtCurrentDate.getText() + "");
        mNotes.setCreatedDate(mTxtCurrentDate.getText() + "");
        mNotes.setBackground(mColor + "");
        mNotes.setAlarm(mStrAlarm);

        insert(mNotes);
        // dem0
        galleryAddPic();
    }

    public void insert(Notes notes) {
        try {
            mDabaseHandler.addNote(notes);
            mDabaseHandler.close();
            Log.d("AddNoteActivity", "Add mNote success.." + mStrAlarm);

        } catch (Exception e) {
            Log.d("AddNoteActivity", "Add new a mNote errorr..." + e.toString());
        }
        // set alarm for this item.
        if (!mStrAlarm.toString().equals(" ")) {
            Log.d("AddNoteActivity", "Add mNote mStrAlarm.." + mStrAlarm);
            startAlert(notes.getID(), mStrAlarm);

        }
    }

    // fuction use to start broadcastreceiver (id )
    public void startAlert(int id, String strAlarm) {
        // get time from spiner
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month);
        calendar.set(Calendar.YEAR, Day);
        calendar.set(Calendar.HOUR, Hour);
        calendar.set(Calendar.MINUTE, Min);

        if(!strAlarm.equals(" ")) {
            Intent intent = new Intent(this, NoteBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getBaseContext(), id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "ALarm set in " + strAlarm, Toast.LENGTH_LONG).show();
        }

    }

    // control the results returned
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int[] arrColor = {getResources().getColor(R.color.color_white), getResources().getColor(R.color.color_yellow),
                getResources().getColor(R.color.color_green), getResources().getColor(R.color.color_blue)};
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_COLOR) {
            Bundle b = data.getBundleExtra("DATA");
            mColor = b.getInt("COLOR");
            Log.d("Add mNote", "Background " + mColor);
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
                    mLlAddNote.setBackgroundColor(arrColor[4]);
                    break;
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                // demo
                mBitmapArrayList.add(bitmap);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                Log.d("insert photo","source "+bitmap.toString());
                mGridImagesAdapter.addItem(resize);
                mGridImagesAdapter.notifyDataSetChanged();

                // demo save images
                try {
                    createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                galleryAddPic();
                /*try {
                    insertImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/

            } else if (requestCode == 1) {
                Bitmap bitmap = null;
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                                getApplicationContext().getContentResolver(), data.getData()
                        );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mBitmapArrayList.add(bitmap);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                mGridImagesAdapter.addItem(resize);
                mGridImagesAdapter.notifyDataSetChanged();
            }

        }
        // try get data
        //mString = getPathFromData(data);


    }

    // datepicker and timepiker dialog
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener, Hour, Min, false);

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, Year, Month, Day);
            default:
                break;
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
            Hour = hourOfDay;
            Min = minutes;
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Year = year;
            Month = month;
            Day = day;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        deleteImage();
    }

    private void deleteImage() {
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure want to delete this ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mGridImagesAdapter.removeItem(pos);
                        mGridImagesAdapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                Log.d("delete photo", "ok");
                builder.create().show();
                return true;
            }
        });

    }

    /**
     * save list images in mNote
     * it demo
     * TODO this
     * @return String bitmap image
     */
    public String saveImages(){
        String s="";
        for (int i = 0; i< mBitmapArrayList.size(); i++){
            Bitmap photo = mBitmapArrayList.get(i);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));

            //s += finalFile.getPath();
            //s += myPath(getApplicationContext(),photo);
        }
        /*Bitmap photo = mBitmapArrayList.get(0);
        Uri tempUri = getImageUri(getBaseContext(), photo);
        s = getImgPath(tempUri);*/
        return s;//demo
    }

    /* TODO this
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public String getImgPath(Uri uri) {
        String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA };
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        Cursor myCursor = this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                largeFileProjection, null, null, largeFileSort);
        String largeImagePath = "";
        try {
            myCursor.moveToFirst();
            largeImagePath = myCursor
                    .getString(myCursor
                            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        } finally {
            myCursor.close();
        }
        return largeImagePath;
    }
    public String getPathFromData(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
    public String myPath(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    }
    */
    /**
     *
     *  SAVE IMAGES FUNCTION : in deploper.android.com
     *
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mString = image.getPath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Error", "occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.notet.activity.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void insertImage() throws FileNotFoundException {
        MediaStore.Images.Media.insertImage(getContentResolver(),mCurrentPhotoPath,"a1","kaka");
    }

}
