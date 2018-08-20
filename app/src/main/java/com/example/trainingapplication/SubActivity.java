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

        // インテントを受け取る
        Intent intent = getIntent();
        mFilePath = intent.getStringExtra("sdPath");
        String selectedFileName = intent.getStringExtra("fileName");
        TextView textView = (TextView)findViewById(R.id.subHeader);
        textView.setText(selectedFileName);

        // DrawSurfaceViewにインテントから受け取ったファイルパスを引き渡す
        mCanvasView = (DrawSurfaceView)findViewById(R.id.canvasView);
        mCanvasView.setPictureView(mFilePath);

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

        // コンテキストメニューを生成
        registerForContextMenu(findViewById(R.id.sub_menu_button));
    }

    // コンテキストメニューの内容を設定
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        // メニュータイトルとメニュー項目を設定
        menu.setHeaderTitle(R.string.context_title);
        menu.add(0, R.id.save_menu, 0, R.string.save_menu);
    }

    // コンテキストメニュー項目押下時の処理
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                // 編集した画像を保存する
                mCanvasView.saveToFile();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
