package jp.co.spookies.android.coloring;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 色選択View
 */
public class PaletteView extends View {

    private int color; // 選択中のカラー
    private Paint paint;
    private ArrayList<Integer> colors = new ArrayList<Integer>(); // パレット内の色

    /**
     * コンストラクタ
     * 
     * @param context
     * @param attrs
     */
    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        setColors();

        setColor(Color.BLACK); // デフォルト色をセット
    }

    /**
     * パレットに色一覧を描画
     * 
     * @param Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Iterator<Integer> ite = colors.iterator();
        int x = 0;
        int y = 0;
        // 画面幅から一つの色を表示する幅を計算
        int eachWidth = (int) Math.floor(this.getWidth() / colors.size());

        // 定義した色数分ループ
        while (ite.hasNext()) {
            int iColor = (Integer) ite.next().intValue();
            paint.setColor(iColor);
            paint.setStyle(Paint.Style.FILL);

            // 各色パレット四角を描画
            Rect r = new Rect();
            int nextX = x + eachWidth;
            int nextY = y + this.getHeight();
            r.set(x, y, nextX, nextY);
            x = nextX;
            canvas.drawRect(r, paint);

            if (color == iColor) {
                // 選択中の色にグラデーション楕円を表示
                Rect markR = new Rect(r.left, r.top + (this.getHeight() / 2),
                        r.right, r.bottom);
                GradientDrawable gradientDrawable = new GradientDrawable(
                        Orientation.LEFT_RIGHT, new int[] { Color.WHITE,
                                iColor, Color.BLACK });
                gradientDrawable.setBounds(markR);
                gradientDrawable.setShape(GradientDrawable.OVAL);
                gradientDrawable.draw(canvas);
            }
        }
    }

    /**
     * タッチされた座標位置から色を取得
     * 
     * @param MotionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 座標
        float x = event.getX();

        // 画面幅から一つの色を表示する幅を計算
        int eachWidth = (int) Math.floor(this.getWidth() / colors.size());
        int pos = (int) (x / eachWidth);
        if (pos < colors.size()) {
            Integer color = colors.get(pos);
            setColor(color);
            invalidate();
        }
        return true;
    }

    /**
     * 利用色をセット 自由に色を変更可能です
     */
    protected void setColors() {
        colors.add(new Integer(Color.WHITE));
        colors.add(new Integer(Color.BLACK));
        colors.add(new Integer(Color.GRAY));
        colors.add(new Integer(Color.rgb(122, 64, 0))); // 茶色
        colors.add(new Integer(Color.RED));
        colors.add(new Integer(Color.rgb(248, 158, 186))); // 薄ムラサキ
        colors.add(new Integer(Color.rgb(254, 198, 173))); // 薄ピンク
        colors.add(new Integer(Color.rgb(254, 147, 33))); // オレンジ
        colors.add(new Integer(Color.YELLOW));
        colors.add(new Integer(Color.rgb(161, 210, 118))); // 薄ミドリ
        colors.add(new Integer(Color.GREEN));
        colors.add(new Integer(Color.rgb(0, 133, 60))); // 濃ミドリ
        colors.add(new Integer(Color.CYAN));
        colors.add(new Integer(Color.BLUE));
        colors.add(new Integer(Color.rgb(2, 62, 132))); // 濃青
        colors.add(new Integer(Color.rgb(120, 65, 156))); // ムラサキ
    }

    /**
     * 選択中の色を取得
     * 
     * @return　選択色
     */
    public int getColor() {
        return this.color;
    }

    /**
     * 選択中の色をセット
     * 
     * @param color
     */
    private void setColor(int color) {
        this.color = color;
    }
}
