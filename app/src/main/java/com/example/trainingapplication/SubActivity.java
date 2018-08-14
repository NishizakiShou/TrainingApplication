package com.example.trainingapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SubActivity extends FragmentActivity implements OnClickListener{

    private DrawSurfaceView mCanvasView;
    private Button mUndoBtn;
    private Button mRedoBtn;
    private Button mResetBtn;
    private String mFilePath;
    private Bitmap mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        mFilePath = intent.getStringExtra("sdPath");
        mPicture = BitmapFactory.decodeFile(mFilePath);
        ((ImageView)findViewById(R.id.selected_photo)).setImageBitmap(mPicture);
        String selectedFileName = intent.getStringExtra("fileName");
        TextView textView = (TextView)findViewById(R.id.subHeader);
        textView.setText(selectedFileName);

        mCanvasView = (DrawSurfaceView)findViewById(R.id.canvasView);

        mUndoBtn = (Button) findViewById(R.id.undoBtn);
        mUndoBtn.setOnClickListener(this);

        mRedoBtn = (Button) findViewById(R.id.redoBtn);
        mRedoBtn.setOnClickListener(this);

        mResetBtn = (Button) findViewById(R.id.resetBtn);
        mResetBtn.setOnClickListener(this);

        registerForContextMenu(findViewById(R.id.sub_menu_button));
    }

    @Override
    public void onClick(View v) {
        if (v == mUndoBtn) {
            mCanvasView.undo();

        } else if (v == mRedoBtn) {
            mCanvasView.redo();

        } else if (v == mResetBtn) {
            mCanvasView.reset();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle(R.string.context_title);
        menu.add(0, R.id.save_menu, 0, "Save");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.save_menu:
                File file = new File(Environment.getExternalStorageDirectory().getPath());
                String filePath = file.getAbsolutePath() + "/";
                filePath += System.currentTimeMillis() + ".JPG";
                File saveFile = new File(filePath);
                while (saveFile.exists()) {
                    filePath = file.getAbsolutePath() + "/" + System.currentTimeMillis() + "JPG";
                    saveFile = new File(filePath);
                }
                try {
                    FileOutputStream outputStream = new FileOutputStream(filePath);
                    mPicture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    registerDatabase(filePath);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void registerDatabase(String file) {
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = SubActivity.this.getContentResolver();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, System.currentTimeMillis() + "/JPG");
        contentValues.put("_data", file);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

    }
}
