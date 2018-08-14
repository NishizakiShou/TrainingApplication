package com.example.trainingapplication;

import android.graphics.Bitmap;

public class ListItem {

    private Bitmap mThumbnail = null;
    private String mFileName = null;
    private String mFileSize = null;

    /**
     *　空のコンストラクタ
     */
    public ListItem() {};

    /**
     * コンストラクタ
     * @param thumbnail　サムネイル画像
     * @param fileName　ファイル名
     * @param fileSize　ファイルサイズ(byte表示)
     */
    public ListItem(Bitmap thumbnail, String fileName, String fileSize) {
        mThumbnail = thumbnail;
        mFileName = fileName;
        mFileSize = fileSize;
    }

    /**
     * サムネイル画像を設定
     * @param thumbnail　サムネイル画像
     */
    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

    /**
     * ファイル名を設定
     * @param fileName
     */
    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    /**
     * ファイルサイズを設定
     * @param fileSize
     */
    public void setFileSize(String fileSize) {
        mFileSize = fileSize;
    }

    /**
     * サムネイル画像を取得
     * @return サムネイル画像
     */
    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    /**
     * ファイル名を取得
     * @return ファイル名
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * ファイルサイズを取得
     * @return ファイルサイズ
     */
    public String getFileSize() {
        return mFileSize;
    }
}
