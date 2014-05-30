package jp.co.spookies.android.coloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * ブラシの種類選択View
 */
public class BrushView extends ImageView {

    // 筆の線の太さ
    public static final int DEFAULT_RADIUS_M = 8;
    // えんぴつの線の太さ
    public static final int DEFAULT_RADIUS_S = 4;

    private int radius; // 選択中の太さ

    /**
     * コンストラクタ
     * 
     * @param context
     */
    public BrushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // デフォルトは筆を選択
        this.radius = DEFAULT_RADIUS_M;
        this.setImageResource(R.drawable.select_brush);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.select_brush);
        this.setMaxWidth(bitmap.getWidth());
        this.setMaxHeight(bitmap.getHeight());
        this.setScaleType(ScaleType.FIT_START);
    }

    /**
     * radiusを取得
     * 
     * @return
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * 筆選択時の処理
     * 
     * @param MotionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 座標位置よりどちらを選ばれたか計算
        if (event.getX() < this.getWidth() / 2) {
            // 太さの設定
            this.radius = DEFAULT_RADIUS_S;
            this.setImageResource(R.drawable.select_pencil);
        } else {
            this.radius = DEFAULT_RADIUS_M;
            this.setImageResource(R.drawable.select_brush);
        }
        // 再描画
        invalidate();
        return true;
    }
}
