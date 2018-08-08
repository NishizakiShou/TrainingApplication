package com.example.trainingapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, Object>> mPictureList = new ArrayList<>();
    private ListView mListView;
    private File[] mFiles;
    private Button mMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sdPath = Environment.getExternalStorageDirectory().getPath();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.endsWith(".jpeg") || s.endsWith(".JPG")) {
                    return true;
                }else {
                    return false;
                }
            }
        };
        mFiles = new File(sdPath).listFiles(filter);

        if(mFiles != null) {
            for (int i = 0 ; i < mFiles.length ; i++ ) {
                String thumbnailPath = mFiles[i].getPath();
                Bitmap thumbnailBm = BitmapFactory.decodeFile(thumbnailPath);
                ImageView thumbnail = new ImageView(this);
                thumbnail.setImageBitmap(thumbnailBm);
                Map<String, Object> mItem = new HashMap<>();
                mItem.put("thumbnail", thumbnail);
                mItem.put("fileName",removeFileExtension(mFiles[i].getName()));
                mItem.put("fileSize",mFiles[i].length() + "byte");

                mPictureList.add(mItem);
            }
            mListView = (ListView)findViewById(R.id.list_view);

            SimpleAdapter mAdapter = new SimpleAdapter(this, mPictureList, R.layout.two_line_list_item, new String[]{"thumbnail", "fileName", "fileSize"}, new int[]{R.id.thumbnail, R.id.fileName, R.id.fileSize});
            mListView.setAdapter(mAdapter);

            registerForContextMenu(mListView);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    String selectedPhotoPath = mFiles[position].getPath();
                    String selectedFileName = mFiles[position].getName();
                    intent.putExtra("sdPath", selectedPhotoPath);
                    intent.putExtra("fileName", selectedFileName);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo adapterInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ListView listView = (ListView)view;
        Map pictureList = (Map)listView.getItemAtPosition(adapterInfo.position);
        listView.setTag(listView.getItemAtPosition(adapterInfo.position));
        menu.setHeaderTitle((String)pictureList.get("fileName"));
        menu.add(0, R.id.delete_menu, 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_menu:
                String selectedPhotoPath = mFiles[info.position].getPath();
                File file = new File(selectedPhotoPath);
                file.delete();
                return true;
                default:
                    return super.onContextItemSelected(item);
        }
    }

    public String removeFileExtension(String fileName) {
        int lastDotPos = fileName.lastIndexOf('.');
        if(lastDotPos == -1) {
            return fileName;
        }else if(lastDotPos == 0) {
            return fileName;
        }else {
            return fileName.substring(0,lastDotPos);
        }
    }
}

