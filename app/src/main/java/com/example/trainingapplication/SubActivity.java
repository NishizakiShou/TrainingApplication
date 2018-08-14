package com.example.trainingapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        menu.add(0, R.id.save_menu, 0, R.string.save_menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                mCanvasView.saveToFile();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
