package com.qdzr.onecar.view;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amitshekhar.utils.ConverterUtils;
import com.umeng.commonsdk.debug.W;

/**
 * 全局水印
 * @author TianM
 */
public class WaterRemark {

    /**
     * 水印文本
     */
    private String mText;

    /**
     * 字体颜色, 十六进制形式,例如0xAEAEAEAE
     */
    private int mTextColor;

    /**
     * 字体大小,单位为sp
     */
    private float mTextSize;
    /**
     * 旋转角度
     */
    private float mRotation;
    private static WaterRemark sInstance;

    private WaterRemark(){
        mText = "";
        mTextColor = 0xAEAEAEAE;
        mTextSize = 18;
        mRotation = -25;
    }

    public static WaterRemark getInstance(){
        if (sInstance == null){
            synchronized (WaterRemark.class){
                sInstance = new WaterRemark();
            }
        }
        return sInstance;
    }

    /**
     * 设置水印文本
     * @param text 文本
     * @return WaterRemark实例
     */
    public WaterRemark setText(String text){
        mText = text;
        return sInstance;
    }

    /**
     * 设置字体大小
     * @param size 大小，单位为sp
     * @return Watermark实例
     */
    public WaterRemark setTextSize(float size){
        mTextSize = size;
        return sInstance;
    }

    /**
     * 设置旋转角度
     * @param degrees
     * @return
     */
    public WaterRemark setRotation(float degrees ){
        mRotation = degrees;
        return sInstance;
    }

    /**
     * 显示水印,铺满整个页面
     * @param activity 活动
     */
    public void show(Activity activity){
        show(activity,mText);
    }

    /**
     * 显示水印,铺满整个页面
     * @param activity 活动
     * @param text     水印
     */
    public void show(Activity activity, String text){

        WaterRemarkDrawable drawable = new WaterRemarkDrawable();
        drawable.mText = text;
        drawable.mTextColor = mTextColor;
        drawable.mTextSize = mTextSize;
        drawable.mRotation = mRotation;

        ViewGroup rootView = activity.findViewById(android.R.id.content);
        FrameLayout layout = new FrameLayout(activity);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackground(drawable);
        rootView.addView(layout);

    }

    private class WaterRemarkDrawable extends Drawable {

        private Paint mPaint;

        /**
         * 水印文本
         */
        private String mText;

        /**
         * 字体颜色,十六进制形式,例如：0xAEAEAEAE
         */
        private int mTextColor;

        /**
         * 字体大小,单位为sp
         */
        private float mTextSize;

        /**
         * 旋转角度
         */
        private float mRotation;

        private WaterRemarkDrawable(){
            mPaint = new Paint();
        }

        /**
         * @param canvas
         */
        @Override
        public void draw(@NonNull Canvas canvas) {
            int width = getBounds().right;
            int height = getBounds().bottom;
            //对角线的长度
            int diagonal = (int) Math.sqrt(width * width + height * height);

            mPaint.setColor(mTextColor);

            mPaint.setTextSize(39);
            mPaint.setAntiAlias(true);
            float textWidth = mPaint.measureText(mText);

            canvas.drawColor(0x00000000);
            canvas.rotate(mRotation);

            int index = 0;
            float fromX;
            //以对角线的长度来做高度，这样可以保证竖屏和横屏整个屏幕都能布满水印
            for (int positionY = diagonal / 10; positionY <= diagonal; positionY += diagonal / 10){
                fromX = -width + (index++ % 2) * textWidth; // 上下两行的X轴起始点不一样，错开显示
                for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                    canvas.drawText(mText, positionX, positionY, mPaint);
                }
            }

            canvas.save();
            canvas.restore();
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

}
