package com.example.trainingapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class DrawSurfaceView extends SurfaceView implements Callback {
    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Path mPath;
    private Bitmap mPictureBitmap = null;
    private Bitmap mLastDrawBitmap;
    private Canvas mLastDrawCanvas;
    private Deque<Path> mUndoStack = new ArrayDeque<Path>();
    private Deque<Path> mRedoStack = new ArrayDeque<Path>();

    public DrawSurfaceView(Context context) {
        super(context);
        // 描画の設定を行う
        init();
    }

    public DrawSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 描画の設定を行う
        init();
    }

    private void init() {
        // 一時的に画面を格納するホルダーを取得する
        mHolder = getHolder();

        // SurfaceViewの背景を透過させる
        // SurfaceViewを画面の最前面に表示させる
        setZOrderOnTop(true);
        // 背景に半透明を設定する
        mHolder.setFormat(PixelFormat.TRANSPARENT);

        // コールバックを設定する
        mHolder.addCallback(this);

        // ペイントを設定する
        mPaint = new Paint();
        // 描画色を設定
        mPaint.setColor(Color.BLACK);
        // 描画スタイルを設定(STROKE：線のみ描画 FILL：塗りつぶしのみ描画)
        mPaint.setStyle(Paint.Style.STROKE);
        // 描画した線の先端の設定(ROUND：先端を丸く)
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 描画をなめらかに見せる設定
        mPaint.setAntiAlias(true);
        // 描画する線の太さを設定
        mPaint.setStrokeWidth(6);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 描画状態を保持するBitmapを生成
        // Bitmapを初期化する
        clearLastDrawBitmap();
        // キャンバスのインスタンスを取得(lockCanvas：あるスレッドが描画処理を行っている間ほかのスレッドからの描画を排他する)
        Canvas canvas = mHolder.lockCanvas();
        drawPicture(canvas);
        // 画面のロックを解除して表示を更新
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Bitmapオブジェクトのメモリ開放処理
        mLastDrawBitmap.recycle();
    }

    private void clearLastDrawBitmap() {
        // Bitmapが用意されていなかった場合にはビットマップを作成する
        if (mLastDrawBitmap == null) {
            //　選択した画像のビットマップをリサイズしてセット(第一引数：リサイズするBitmap、第二引数：変更後の横幅、第三引数：変更後の縦幅、第四引数：画質をなめらかにするか)
            mPictureBitmap = Bitmap.createScaledBitmap(mPictureBitmap, 1050, 1500, false);
            // リサイズした画像と同じ大きさで、描画するビットマップを作成(第三引数：ARGB_8888 = ARGBで、0～256段階の色を使用 [ARGB_4444ならば、0～127段階の色を使用、Aは0が透明で255が完全な不透明])
            mLastDrawBitmap = Bitmap.createBitmap(mPictureBitmap.getWidth(), mPictureBitmap.getHeight(),
                    Config.ARGB_8888);
        }

        // Canvasが用意されていなかった場合にはCanvasを作成する
        if (mLastDrawCanvas == null) {
            mLastDrawCanvas = new Canvas(mLastDrawBitmap);
        }
        // Canvas全体を透明色塗りつぶす
        mLastDrawCanvas.drawColor(0, Mode.CLEAR);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 押下位置のx軸とy軸を渡す
                onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                // 移動した位置のx軸とy軸を渡す
                onTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                // 指を離した位置のx軸とy軸を渡す
                onTouchUp(event.getX(), event.getY());
                break;
            default:
        }
        return true;
    }

    private void onTouchDown(float x, float y) {
        mPath = new Path();
        // パスの開始地点を決定
        mPath.moveTo(x, y);
    }

    private void onTouchMove(float x, float y) {
        // メソッドで描画するポイントを追加
        mPath.lineTo(x, y);
        drawLine(mPath);
    }

    private void onTouchUp(float x, float y) {
        // 指を離した位置を追加
        mPath.lineTo(x, y);
        drawLine(mPath);
        // Canvasに、押下してから離すまでのPathを描画する
        mLastDrawCanvas.drawPath(mPath, mPaint);
        // undoにパスのデータを追加する
        mUndoStack.addLast(mPath);
        // redoのデータをクリアする
        mRedoStack.clear();
    }

    private void drawLine(Path path) {
        // ロックしてキャンバスを取得する
        Canvas canvas = mHolder.lockCanvas();

        // キャンバスをクリアする
        canvas.drawColor(0, Mode.CLEAR);

        // 前回描画したビットマップをキャンバスに描画する
        canvas.drawBitmap(mLastDrawBitmap, 0, 0, null);

        // パスを描画する
        canvas.drawPath(path, mPaint);

        // ロックを外す
        mHolder.unlockCanvasAndPost(canvas);
    }

    // 選択された画像のビットマップを描画する
    public void drawPicture(Canvas canvas) {
        mPictureBitmap = Bitmap.createScaledBitmap(mPictureBitmap, 1050, 1500, false);
        canvas.drawBitmap(mLastDrawBitmap, 0, 0, null);
        canvas.drawBitmap(mPictureBitmap, 0, 0, mPaint);
        // Canvasに選択した画像を描画する
        mLastDrawCanvas.drawBitmap(mPictureBitmap, 0, 0, mPaint);
    }

    // SubActivityから渡されたファイルパスからビットマップを作成する
    public void setPictureView(String filePath) {
        mPictureBitmap = BitmapFactory.decodeFile(filePath);
    }

    public void undo() {
        if (mUndoStack.isEmpty()) {
            return;
        }

        // undoスタックからパスを取り出し、redoスタックに格納する
        Path lastUndoPath = mUndoStack.removeLast();
        mRedoStack.addLast(lastUndoPath);

        // ロックしてキャンバスを取得する
        Canvas canvas = mHolder.lockCanvas();

        // キャンバスをクリアする
        canvas.drawColor(0, Mode.CLEAR);

        // 描画状態を保持するBitmapをクリアする
        clearLastDrawBitmap();

        drawPicture(canvas);

        // パスを描画する
        for (Path path : mUndoStack) {
            canvas.drawPath(path, mPaint);
            mLastDrawCanvas.drawPath(path, mPaint);
        }

        // ロックを外す
        mHolder.unlockCanvasAndPost(canvas);


    }

    public void redo() {
        if (mRedoStack.isEmpty()) {
            return;
        }

        // redoスタックからパスを取り出し、undoスタックに格納する
        Path lastRedoPath = mRedoStack.removeLast();
        mUndoStack.addLast(lastRedoPath);

        // パスを描画する
        drawLine(lastRedoPath);

        mLastDrawCanvas.drawPath(lastRedoPath, mPaint);
    }

    public void reset() {
        // undoとredoに保存されたデータをクリアする
        mUndoStack.clear();
        mRedoStack.clear();

        // 描画状態を保持するBitmapをクリアする
        clearLastDrawBitmap();

        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(0, Mode.CLEAR);
        drawPicture(canvas);
        mHolder.unlockCanvasAndPost(canvas);


    }

    // 編集した画像の保存処理
    public void saveToFile() {
        // 保存するディレクトリを設定
        File file = new File(Environment.getExternalStorageDirectory().getPath());

        // 保存するためのファイル名を生成する
        // 絶対パス名と、保存処理時の現在時刻を合わせたPNG形式のファイル名を生成
        String AttachName = file.getAbsolutePath() + "/";
        AttachName += System.currentTimeMillis() + ".png";
        File saveFile = new File(AttachName);
        // 保存するファイル名がすでに存在していた場合、ファイル名を変更
        while (saveFile.exists()) {
            AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + "a.png";
            saveFile = new File(AttachName);
        }
        try {
            // Fileにデータを書き込む
            // ファイルに書き込むための出力ストリームを生成(プログラムからファイルにデータを書き込む)
            FileOutputStream outputStream = new FileOutputStream(AttachName);
            // 作成したBitmapからPNGファイルを作成する。(第一引数：作成するフォーマット形式、第二引数：画質[0～100、0:低画質、100:高画質]、第三引数：ファイル出力用のストリーム)
            mLastDrawBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            // バッファリングされていたすべての出力バイトを強制的に書き込む
            outputStream.flush();
            // 出力ストリームを閉じ、このストリームに関するすべてのシステムリソースを開放する
            outputStream.close();
        } catch (Exception e) {
            // 例外情報を標準エラー出力に出力
            e.printStackTrace();
        }
    }
}
