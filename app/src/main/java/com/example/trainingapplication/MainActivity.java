package com.example.trainingapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String,String>> pictureList = new ArrayList<Map<String, String>>();
    private ListView mListView;
    private File[] mFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sdPath = Environment.getExternalStorageDirectory().getPath();
        mFiles = new File(sdPath).listFiles();

        if(mFiles != null) {
            for(int i = 0 ; i < mFiles.length ; i++ ) {
                if(mFiles[i].isFile()) {
                    Map<String,String> mItem = new HashMap<String, String>();
                    mItem.put("fileName",removeFileExtension(mFiles[i].getName()));
                    mItem.put("fileSize",mFiles[i].length()+"byte");
                    pictureList.add(mItem);
                }
            }
            mListView = (ListView)findViewById(R.id.list_view);

            SimpleAdapter mAdapter = new SimpleAdapter(this,pictureList, R.layout.simple_expandable_list_item_2,new String[]{"fileName","fileSize"},new int[]{R.id.text1,R.id.text2});
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    ListView listView = (ListView)adapterView;
                    int item = (int)listView.getItemAtPosition(position);
                    Intent.putExtra("picture",item);

                    startActivities(intent));
                }
            });
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

