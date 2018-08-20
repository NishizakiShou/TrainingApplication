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

        // ListItemの要素を保存するArrayListを生成
        ArrayList<ListItem> listItems = new ArrayList<>();

        // 内部ストレージ直下のパスを取得
        String sdPath = Environment.getExternalStorageDirectory().getPath();

        // mFilesに入れるファイルのファイル名にフィルターをかけて、特定のファイル名のみをtrueで返す
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String fileName) {
                if (fileName.endsWith(".jpeg") || fileName.endsWith(".JPG") || fileName.endsWith(".jpg") || fileName.endsWith(".PNG") || fileName.endsWith(".png")) {
                    return true;
                }else {
                    return false;
                }
            }
        };
        // 内部ストレージ直下の、フィルターをかけた特定のファイル一覧を取得
        mFiles = new File(sdPath).listFiles(filter);

        // ListItemに保存するための要素を取得して、ListItemに保存
        if(mFiles != null) {
            for (int i = 0 ; i < mFiles.length ; i++ ) {
                // サムネイル用の画像をビットマップに変換
                String thumbnailPath = mFiles[i].getPath();
                Bitmap thumbnailBm = BitmapFactory.decodeFile(thumbnailPath);
                // 拡張子を除いたファイル名を取得
                String fileName = removeFileExtension(mFiles[i].getName());
                // ファイルサイズを取得
                String fileSize = mFiles[i].length() + "byte";
                // ListItemに要素を保存
                ListItem item = new ListItem(thumbnailBm, fileName, fileSize);
                listItems.add(item);
            }

            // ListViewにListItemを表示させるためのアダプターを生成
            ImageArrayAdapter adapter = new ImageArrayAdapter(this, R.layout.two_line_list_item, listItems);
            mListView.setAdapter(adapter);

            mMenuButton = findViewById(R.id.context_menu_button);
            // コンテキストメニューを生成
            registerForContextMenu(mListView);

            // リストビューの項目が押されたときの処理
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // インテントを生成して、押下位置の画像ファイルパスとファイル名をサブアクティビィティに渡して画面遷移する
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

    // コンテキストメニューの内容
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        // メニュータイトルと、メニュー項目を設定
        // メニュータイトルを設定
        menu.setHeaderTitle(R.string.context_title);
        // メニュー項目を設定。(第一引数:グループID、第二引数:アイテムID、第三引数:メニューの表示順、第四引数:メニューに表示する文言)
        menu.add(0, R.id.delete_menu, 0, R.string.delete_menu);
    }

    // コンテキストメニュー項目押下時の処理
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_menu:
                // 選択したファイルを削除
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

