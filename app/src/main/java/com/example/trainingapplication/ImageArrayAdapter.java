package com.example.trainingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {

    private int mResourceId;
    private List<ListItem> mItems;
    private LayoutInflater mInflater;
    public MainActivity mMainActivity;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resourceId リソースID
     * @param items リストビューの要素
     */
    public ImageArrayAdapter(Context context, int resourceId, List<ListItem> items) {
        super(context, resourceId, items);

        this.mResourceId = resourceId;
        this.mItems = items;
        // システムサービスからLayoutInflaterを取得(指定したxmlレイアウトリソースを利用できるようになる)
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        // 再利用可能なView(convertView)があるか判断する。あればconvertViewをViewにする。
        if (convertView != null) {
            view = convertView;
        } else {
            // 第一引数:リソースID、第二引数:リソースの親となるレイアウト
            view = this.mInflater.inflate(this.mResourceId, null);
        }

        // 渡された位置(position)からデータを取得する
        ListItem item = this.mItems.get(position);

        // サムネイルをセット
        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(item.getThumbnail());

        // ファイル名をセット
        TextView fileName = (TextView) view.findViewById(R.id.fileName);
        fileName.setText(item.getFileName());

        // ファイルサイズをセット
        TextView fileSize = (TextView) view.findViewById(R.id.fileSize);
        fileSize.setText(item.getFileSize());

        // オプションメニューをセット
        ImageButton optionMenu = (ImageButton) view.findViewById(R.id.context_menu_button);
        optionMenu.setTag(position);

        return view;
    }
}
