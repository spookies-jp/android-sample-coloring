package jp.co.spookies.android.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

class MyCanvasView extends View {

    private Paint paint;
    private Paint bitmapPaint;
    private Path path; // タッチした時から、指を離したときまでの軌跡
    private Bitmap bitmap; //
    private Bitmap undoBitmap; // Undo用
    private Canvas canvas;
    private float xPos, yPos; // 現在の座標
    private PaletteView paletteView;
    private BrushView brushView;
    private static final float TOUCH_TOLERANCE = 4; // タッチの閾値

    /**
     * コンストラクタ
     * 
     * @param context
     * @param attrs
     */
    public MyCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        BlurMaskFilter mastFilter = new BlurMaskFilter(4.0f,
                BlurMaskFilter.Blur.INNER);

        this.path = new Path();

        this.paint = new Paint();
        this.paint.setStrokeWidth(BrushView.DEFAULT_RADIUS_M);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Paint.Join.ROUND); // 繋ぎ目
        this.paint.setStrokeCap(Paint.Cap.ROUND); // 線の両端形状
        this.paint.setStyle(Paint.Style.STROKE); // 強さ
        this.paint.setMaskFilter(mastFilter); // フィルター

        this.bitmapPaint = new Paint();
    }

    /**
     * パレットViewをセット
     * @param view
     */
    public void setPaletteView(PaletteView view) {
        this.paletteView = view;
    }

    /**
     * ブラシViewをセット
     * @param view
     */
    public void setBrushView(BrushView view) {
        this.brushView = view;
    }

    /**
     * 色をセット
     * @param color
     */
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    /**
     * 線の太さをセット
     * @param radius
     */
    public void setRadius(int radius) {
        this.paint.setStrokeWidth(radius);
    }

    /**
     * 描画ビットマップを取得
     * @return
     */
    public Bitmap getMBitmap() {
        return this.bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 初期化
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, bitmapPaint); // タッチ前の画面
        }
        this.paint.setColor(this.paletteView.getColor());
        this.paint.setStrokeWidth(this.brushView.getRadius());
        canvas.drawPath(path, paint); // タッチ中の指の軌跡を描画
    }

    /**
     * タッチ開始イベント
     * 
     * @param x
     * @param y
     */
    private void start(float x, float y) {
        path.reset();
        path.moveTo(x, y); // 軌跡の開始
        xPos = x;
        yPos = y;
    }

    /**
     * タッチ中イベント
     * 
     * @param x
     * @param y
     */
    private void move(float x, float y) {
        // 移動座標
        float dx = Math.abs(x - xPos);
        float dy = Math.abs(y - yPos);

        // 移動量が閾値を超えた場合のみ処理
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(xPos, yPos, (x + xPos) / 2, (y + yPos) / 2); // 二次ベジェ曲線で軌跡をセット
            xPos = x;
            yPos = y;
        }
    }

    /**
     * タッチ終了イベント
     */
    private void end() {
        undoBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // undo用にbitmapをコピーして保持
        path.lineTo(xPos, yPos);
        canvas.drawPath(path, paint); // タッチ中の軌跡を描画
        path.reset(); // 軌跡をリセット
    }

    /**
     * 画面クリア
     */
    public void clear() {
        undoBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        path.reset();
        invalidate();
    }

    /**
     * ひとつ前の状態に戻す
     */
    public void undo() {
        if (undoBitmap != null) {
            bitmap = undoBitmap;
            canvas.setBitmap(bitmap);
            path.reset();
        }
        invalidate();
    }

    /**
     * タッチイベント時
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("palette", "myCanvasView.onTouchEvent");
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            start(x, y);
            invalidate(); // 再描画
            break;
        case MotionEvent.ACTION_MOVE:
            move(x, y);
            invalidate(); // 再描画
            break;
        case MotionEvent.ACTION_UP:
            end();
            invalidate(); // 再描画
            break;
        }
        return true;
    }
}