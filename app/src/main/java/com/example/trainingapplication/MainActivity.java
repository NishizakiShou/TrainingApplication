package com.example.trainingapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private File[] mFiles;
    private Button mMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.list_view);

        ArrayList<ListItem> listItems = new ArrayList<>();

        String sdPath = Environment.getExternalStorageDirectory().getPath();
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.endsWith(".jpeg") || s.endsWith(".JPG") || s.endsWith(".jpg") || s.endsWith(".PNG") || s.endsWith(".png")) {
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

                String fileName = removeFileExtension(mFiles[i].getName());

                String fileSize = mFiles[i].length() + "byte";

                ListItem item = new ListItem(thumbnailBm, fileName, fileSize);

                listItems.add(item);
            }

            ImageArrayAdapter adapter = new ImageArrayAdapter(this, R.layout.two_line_list_item, listItems);

            mListView.setAdapter(adapter);

            mMenuButton = findViewById(R.id.context_menu_button);
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
        menu.setHeaderTitle(R.string.context_title);
        // 第一引数:グループID、第二引数:アイテムID、第三引数:メニューの表示順、第四引数:メニューに表示する文言
        menu.add(0, R.id.delete_menu, 0, R.string.delete_menu);
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

    /**
     * ファイル名から拡張子を除いた名前を取得
     * @param fileName　ファイル名
     * @return 拡張子を除いたファイル名
     */
    public String removeFileExtension(String fileName) {
        //文字列の末尾から検索して初めに見つかった位置のインデックス(見つからなければ「－1」が返される)
        int lastDotPos = fileName.lastIndexOf('.');

        if(lastDotPos == -1) {
            return fileName;
        }else if(lastDotPos == 0) {
            return fileName;
        }else {
            // 拡張子を除いたファイル名を取得(第一引数:開始インデックス[この値を含む]、第二引数:終了インデックス[この値を含まない])
            return fileName.substring(0,lastDotPos);
        }
    }
}

