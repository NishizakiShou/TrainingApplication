package com.example.trainingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends FragmentActivity{

    private DrawSurfaceView mCanvasView;
    public Button mUndoBtn;
    public Button mRedoBtn;
    public Button mResetBtn;
    public String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        mFilePath = intent.getStringExtra("sdPath");
        String selectedFileName = intent.getStringExtra("fileName");
        TextView textView = (TextView)findViewById(R.id.subHeader);
        textView.setText(selectedFileName);

        mCanvasView = (DrawSurfaceView)findViewById(R.id.canvasView);
        mCanvasView.setPictureView(mFilePath);

        mUndoBtn = (Button) findViewById(R.id.undoBtn);
        mUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.undo();
            }
        });

        mRedoBtn = (Button) findViewById(R.id.redoBtn);
        mRedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.redo();
            }
        });

        mResetBtn = (Button) findViewById(R.id.resetBtn);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.reset();
            }
        });

        registerForContextMenu(findViewById(R.id.sub_menu_button));
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
