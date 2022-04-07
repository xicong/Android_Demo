package com.cong.demo.zidingyiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import tv.danmaku.ijk.media.player.pragma.DebugLog;

public class RegionView extends View {
    private Paint mPaint;
    private Path mPath;
    private RectF mBigRectF;
    private RectF mSmallRectF;
    // 目标图形区域
    private Region targetGraphRegion;
    // 模板边界区域（范围更大）
    private Region templateRegion;

    // 按下时的区域标识
    private int touchFlag = -1;
    // 当前的区域标识
    private int currentFlag = -1;

    public RegionView(Context context) {
        this(context, null);
    }

    public RegionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
        targetGraphRegion = new Region();
        LogUtils.e("初始化的目标图形区域#targetGraphRegion：" + targetGraphRegion.toString());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 创建模板边界区域
        templateRegion = new Region(0, 0, w, h);
        mBigRectF = new RectF(w / 2 - 400, h / 2 - 400, w / 2 + 400, h / 2 + 400);
        mSmallRectF = new RectF(w / 2 - 200, h / 2 - 200, w / 2 + 200, h / 2 + 200);
    }

    // 事件处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int) event.getX();
        int py = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentFlag = touchFlag = containsPoint(px, py);
                break;
            case MotionEvent.ACTION_MOVE:
                currentFlag = containsPoint(px, py);
                break;
            case MotionEvent.ACTION_UP:// 释放手指时复位
                currentFlag = containsPoint(px, py);
                // 如果按下的点和释放的点在同一区域而且是目标区域，则认为是点击事件（目标区域被点击）
                if (currentFlag == touchFlag && currentFlag != -1) {
                    LogUtils.e("--在目标图形区域内被点击--");
                    if (mListener != null) {
                        mListener.onRegionClickListener(px, py);
                    }
                }
                currentFlag = touchFlag = -1;
                break;
            case MotionEvent.ACTION_CANCEL:// 被上层控件拦截时复位
                currentFlag = touchFlag = -1;
                break;
        }
        invalidate();
        return true;
    }

    // 目标图形区域是否包含指定的点
    private int containsPoint(int px, int py) {
        return targetGraphRegion.contains(px, py) ? 1 : -1;
    }

    // 图形绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制扇形
        mPath.reset();
        // 添加一个大圆弧到path
        mPath.addArc(mBigRectF, 230, 82);
        // 添加一个小圆弧到path( 起点与上次最后一个坐标点连接) 310-230=80
        mPath.arcTo(mSmallRectF, 310, -82);
        // 连接成闭合曲线
        mPath.close();

        // 这将在templateRegion模板区域上裁剪出一个与路径mPath所绘制的目标图形相同的区域, 然后赋给targetGraphRegion。
        // 如果结果区域targetGraphRegion非空，则返回true。
        // 注意：不管mPath所绘制的目标图形是否闭合，生成的目标区域targetGraphRegion始终是闭合的。
        boolean success = targetGraphRegion.setPath(mPath, templateRegion);
        LogUtils.e("success：" + success);
        LogUtils.e("生成后的目标图形区域#targetGraphRegion：" + targetGraphRegion.toString());
        if (!targetGraphRegion.isEmpty()) {
            LogUtils.e("targetGraphRegion.getBounds()：" + targetGraphRegion.getBounds().toString());
        }

        // 绘制Path图形
        // 添加触摸变色效果
        if (currentFlag == touchFlag && currentFlag != -1) {
            mPaint.setColor(Color.YELLOW);
        } else {
            mPaint.setColor(Color.RED);
        }
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(20);
        canvas.drawPath(mPath, mPaint);
    }

    private RegionListener mListener;

    public void setListener(RegionListener listener) {
        this.mListener = listener;
    }

    public interface RegionListener {
        void onRegionClickListener(int x, int y);
    }

}