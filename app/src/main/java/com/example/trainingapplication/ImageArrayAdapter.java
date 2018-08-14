package com.example.trainingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {

    private int resourceId;
    private List<ListItem> items;
    private LayoutInflater inflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resourceId リソースID
     * @param items リストビューの要素
     */
    public ImageArrayAdapter(Context context, int resourceId, List<ListItem> items) {
        super(context, resourceId, items);

        this.resourceId = resourceId;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }

        ListItem item = this.items.get(position);

        //サムネイルをセット
        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(item.getThumbnail());

        //ファイル名をセット
        TextView fileName = (TextView)view.findViewById(R.id.fileName);
        fileName.setText(item.getFileName());

        //ファイルサイズをセット
        TextView fileSize = (TextView)view.findViewById(R.id.fileSize);
        fileSize.setText(item.getFileSize());

        return view;
    }
}
