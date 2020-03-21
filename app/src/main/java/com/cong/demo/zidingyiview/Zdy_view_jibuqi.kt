package com.cong.demo.zidingyiview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.cong.demo.R

/**
 * https://www.bilibili.com/video/av88247231
 */
class Zdy_view_jibuqi : View{

    private var mOutColor = Color.RED
    private var mInnerColor =Color.BLUE
    private var mBorderWidth = 5
    private var mStepText:Float = 1F
    private var mStepTextColor = Color.GRAY
    private var mStepTextSize = 10

    private var mOutPaint:Paint?=null
    private var mInnrPaint:Paint?=null
    private var mTextPaint:Paint?=null
    
    constructor(context: Context):this(context,null){}
    
    constructor(context: Context,atts:AttributeSet?):this(context,atts,0){
    }

    constructor(context: Context,atts: AttributeSet?,defStyleAttr:Int):super(context,atts,defStyleAttr){
         // 1 分析需求
        // 2  自定义属性
        // 3 onMeasure
        // 4  获得自定义属性
        // 5 绘制 外圆弧  内圆弧  文字
        // 6 其他

        val  array=context.obtainStyledAttributes(atts, R.styleable.Zdy_view_jibuqi)
        
        mOutColor=array.getColor(R.styleable.Zdy_view_jibuqi_jbq_outer_color,mOutColor)
        mInnerColor=array.getColor(R.styleable.Zdy_view_jibuqi_jbq_inner_color,mInnerColor)
        mBorderWidth= array.getDimension(R.styleable.Zdy_view_jibuqi_jbq_boder_width,
            mBorderWidth.toFloat()
        ).toInt()
        mStepText= array.getFloat(R.styleable.Zdy_view_jibuqi_jbq_text,mStepText)
        mStepTextColor=array.getColor(R.styleable.Zdy_view_jibuqi_jbq_text_color,mStepTextColor)
        mStepTextSize= array.getDimension(R.styleable.Zdy_view_jibuqi_jbq_text_size,
            mStepTextSize.toFloat()
        ).toInt()
        
        array.recycle()

        //外圈画笔
        mOutPaint=Paint()
        mOutPaint!!.isAntiAlias=true
        mOutPaint!!.setColor(mOutColor)
        mOutPaint!!.strokeWidth= mBorderWidth.toFloat()
        mOutPaint!!.style=Paint.Style.STROKE
        mOutPaint!!.strokeCap=Paint.Cap.ROUND
        
        //内圆弧画笔
        mInnrPaint=Paint()
        mInnrPaint!!.isAntiAlias=true
        mInnrPaint!!.setColor(mInnerColor)
        mInnrPaint!!.strokeWidth= mBorderWidth.toFloat()  //设置宽度
        mInnrPaint!!.style=Paint.Style.STROKE   //设置空心
        mInnrPaint!!.strokeCap=Paint.Cap.ROUND  //设置圆角
        
        //文字画笔
        mTextPaint=Paint()
        mTextPaint!!.isAntiAlias=true
        mTextPaint!!.color=mStepTextColor
        mTextPaint!!.textSize= mStepTextSize.toFloat()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        
        val width=MeasureSpec.getSize(widthMeasureSpec)
        val height=MeasureSpec.getSize(heightMeasureSpec)
        
        if (width>height){
            setMeasuredDimension(height,height)
        }else{
            setMeasuredDimension(width,width)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 5.1 绘制内圆弧
        // 5.2 绘制外圆弧
        // 5.3 绘制文字
        
        //rect  巨型
        //startAngle  开发绘制的位置
        //sweepAngle  旋转过的角度
        //
        
        //绘制内圆弧
        val center=width/2
        val radius=(width-mBorderWidth)/2
        val rectF=RectF((mBorderWidth/2).toFloat(), (mBorderWidth/2).toFloat(), (center+radius).toFloat(), (center+radius).toFloat())
        mOutPaint?.let { canvas!!.drawArc(rectF, 135F, 270F,false, it) }
   
        //绘制外圆弧
        val maxStep:Float=1000F
        if (maxStep<0){
            return
        }
        val bz = mStepText/maxStep
        mInnrPaint?.let { canvas!!.drawArc(rectF, 135F, 270*bz,false, it) }
        
        //绘制文字
        val mText:String="${String.format("%.1f", mStepText)}"
        val  rect=Rect()
        mTextPaint!!.getTextBounds(mText,0,mText.length,rect)
        val dx=width/2-rect.width()/2
        val fontMetricsInt= mTextPaint!!.getFontMetricsInt()  //获取字体相关的一些东西
        val dy=fontMetricsInt.bottom-fontMetricsInt.top
        val baseline=height/2+dy/2
        canvas!!.drawText(mText, dx.toFloat(), baseline.toFloat(), mTextPaint!!)   //基线显示
//        canvas!!.drawText(mText,dx.toFloat() ,(height/2+rect.height()/2).toFloat(), mTextPaint!!)  //居中显示
    }

     fun setStepText(stepText: Float){
        mStepText=stepText
        invalidate()
    }
}