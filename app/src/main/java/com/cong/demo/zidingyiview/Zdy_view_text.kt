package com.cong.demo.zidingyiview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.cong.demo.R

/**
 * https://www.bilibili.com/video/av80992966
 */
class Zdy_view_text : View{

    //文字内容
    var mText="text"
    //文字大小
    var mTextSize:Int=20
    //文字颜色
    var mTextColor: Int = Color.BLUE
    //画笔
    var mPaint:Paint? = null
    
    
    /**
     * 在代码中实例化的时候调用
     */
    constructor(context: Context):this(context,null){
    }

    /**
     * 布局文件实例化调用
     */
    constructor(context: Context,atts:AttributeSet?):this(context,atts,0){
    }

    /**
     * 布局文件实例化的时候调用
     * defStyleAttr:Int  可以把布局属性写到style里面去
     */
    constructor(context: Context,atts: AttributeSet?,defStyleAttr:Int):super(context,atts,defStyleAttr){

        //获取自定义属性
        val array=context.obtainStyledAttributes(atts, R.styleable.Zdy_view_text)

        mText= array.getString(R.styleable.Zdy_view_text_my_text).toString()
        mTextColor=array.getColor(R.styleable.Zdy_view_text_my_text_color,mTextColor)
        mTextSize=array.getDimensionPixelSize(R.styleable.Zdy_view_text_my_text_size,mTextSize)

        //属性及时回收
        array.recycle()

        mPaint= Paint()
        //去锯齿
        mPaint!!.isAntiAlias=true
        mPaint!!.color=mTextColor
        mPaint!!.textSize= mTextSize.toFloat()
        
    }

    /**
     * 控制计算自定义view的大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        
        //获取布局中设置的wrap_content，match_parent，fill_parent
        val widthMode=MeasureSpec.getMode(widthMeasureSpec)
        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        
        //获取宽高
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        /**
         * AT_MOST   wrap_content   适应内部的内容的大小
         * EXACTLY  match_parent  或者具体多少dp    确切的大小
         * UNSPECIFIED 尽可能大的   比如Scrollview与Listview 结合使用的时候只显示一个item
         */
        if(widthMode==MeasureSpec.AT_MOST){
            //测量宽 高
            val bouns=Rect()
            mPaint?.getTextBounds(mText,0,mText.length,bouns)
            width=bouns.width()
            //增加padding的距离
            width=width+paddingLeft+paddingRight
        }else if(widthMode==MeasureSpec.EXACTLY){
            
        }else if(widthMode==MeasureSpec.UNSPECIFIED){
            
        }

        if(heightMode==MeasureSpec.AT_MOST){
            //测量宽 高
            val bouns=Rect()
            mPaint?.getTextBounds(mText,0,mText.length,bouns)
            height=bouns.height()
            //增加padding的距离
            height=height+paddingTop+paddingBottom
        }else if(heightMode==MeasureSpec.EXACTLY){

        }else if(heightMode==MeasureSpec.UNSPECIFIED){

        }
        
        setMeasuredDimension(width,height)
    }

    /**
     * 注意，该方法会被执行多次
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        //获取baseline
        val fontMetrics= mPaint?.getFontMetrics()
        val dy=((fontMetrics!!.bottom-fontMetrics.top)/2)-fontMetrics.bottom
        //height
        val baseline=height/2+dy

        //绘制文字
        mPaint?.let { canvas!!.drawText(mText, paddingLeft.toFloat(), baseline, it) }
        
    }
}