package com.example.trainingapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

public class SubActivity extends FragmentActivity implements OnClickListener{

    private DrawSurfaceView mCanvasView;
    private Button mUndoBtn;
    private Button mRedoBtn;
    private Button mResetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        String selectedPhotoPath = intent.getStringExtra("sdPath");
        Bitmap picture = BitmapFactory.decodeFile(selectedPhotoPath);
        ((ImageView)findViewById(R.id.selected_photo)).setImageBitmap(picture);

        mCanvasView = (DrawSurfaceView) findViewById(R.id.canvasView);

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

        menu.setHeaderTitle("MENU");
        menu.add(0, R.id.save_menu, 0, "Save");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.save_menu:

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
