package com.example.trainingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class SubActivity extends FragmentActivity{

    private DrawSurfaceView mCanvasView;
    private ImageButton mMenuButton;
    public Button mUndoBtn;
    public Button mRedoBtn;
    public Button mResetBtn;
    public String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        // インテントを受け取る
        Intent intent = getIntent();
        mFilePath = intent.getStringExtra("sdPath");
        String selectedFileName = intent.getStringExtra("fileName");
        TextView textView = (TextView)findViewById(R.id.subHeader);
        textView.setText(selectedFileName);

        // DrawSurfaceViewにインテントから受け取ったファイルパスを引き渡す
        mCanvasView = (DrawSurfaceView)findViewById(R.id.canvasView);
        mCanvasView.setPictureView(mFilePath);

        mMenuButton = (ImageButton) findViewById(R.id.sub_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // UNDOボタンを設定
        mUndoBtn = (Button) findViewById(R.id.undoBtn);
        mUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.undo();
            }
        });

        // REDOボタンを設定
        mRedoBtn = (Button) findViewById(R.id.redoBtn);
        mRedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.redo();
            }
        });

        // RESETボタンを設定
        mResetBtn = (Button) findViewById(R.id.resetBtn);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.reset();
            }
        });


    }

    public void showPopup(View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        final MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sub_activity_menu1, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.save_menu:
                        // 編集した画像を保存する
                        mCanvasView.saveToFile();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
