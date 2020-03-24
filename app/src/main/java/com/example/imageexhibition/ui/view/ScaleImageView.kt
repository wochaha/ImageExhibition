package com.example.imageexhibition.ui.view

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import kotlin.math.min
import kotlin.math.sqrt


class ScaleImageView  : androidx.appcompat.widget.AppCompatImageView,ScaleGestureDetector.OnScaleGestureListener,View.OnTouchListener,ViewTreeObserver.OnGlobalLayoutListener {

    private var mInitScale = 1.0f
    private var mMaxScale = mInitScale*3
    private var mMidScale = mInitScale
    private var mOnce:Boolean = true
    private val mMatrixValues:FloatArray = FloatArray(9)

    private var mScaleGestureDetector:ScaleGestureDetector

    private val mScaleMatrix = Matrix()

    private var mGestureDetector:GestureDetector
    private var mIsAutoScale:Boolean = false

    private val viewConfiguration:ViewConfiguration

    private var mTouchSlop:Int = 0

    private var mLastX:Float = 0f
    private var mLastY:Float = 0f

    private var mIsCanDrag:Boolean = false
    private var mLastPointerCount:Int = 1

    private var isCheckTopAndBottom = true
    private var isCheckLeftAndRight = true

    fun getScale():Float{
        mScaleMatrix.getValues(mMatrixValues)
        return mMatrixValues[Matrix.MSCALE_X]
    }

    constructor(context: Context):this(context,null){

    }

    constructor(context: Context,attributes: AttributeSet?):super(context,attributes){
        super.setScaleType(ScaleType.MATRIX)
        viewConfiguration = ViewConfiguration.get(context)
        mTouchSlop = viewConfiguration.scaledTouchSlop
        mGestureDetector = GestureDetector(context,object : GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (mIsAutoScale){
                    return true
                }

                if (e != null){
                    val x = e.x
                    val y = e.y

                    if (getScale() < mMidScale){
                        postDelayed(
                            AutoScaleRunnable(
                                mMidScale,
                                x,
                                y
                            ),16)
                        mIsAutoScale = true
                    }else{
                        postDelayed(
                            AutoScaleRunnable(
                                mInitScale,
                                x,
                                y
                            ),16)
                        mIsAutoScale = true
                    }
                }

                return true
            }
        })
        mScaleGestureDetector = ScaleGestureDetector(context,this)
        setOnTouchListener(this)
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {

    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        val scale = getScale()
        if (detector != null){
            var scaleFactor = detector.scaleFactor

            if (drawable == null){
                return true
            }

            if ((scale < mMaxScale && scaleFactor >= 1.0f)
                ||(scale > mInitScale && scaleFactor < 1.0f)){
                if (scaleFactor * scale < mInitScale){
                    scaleFactor = mInitScale / scale
                }
                if (scaleFactor * scale > mMaxScale){
                    scaleFactor = mMaxScale / scale
                }

                mScaleMatrix.postScale(scaleFactor,scaleFactor,detector.focusX,detector.focusY)
            }
        }

        return true
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (mGestureDetector.onTouchEvent(event)){
            return true
        }
        mScaleGestureDetector.onTouchEvent(event)

        var x = 0f
        var y = 0f

        if (event != null){
            val pointerCount = event.pointerCount
            for (i in 0 until pointerCount) {
                x += event.getX(i)
                y += event.getY(i)
            }
            x /= pointerCount
            y /= pointerCount

            if (pointerCount != mLastPointerCount){
                mIsCanDrag = false
                mLastX = x
                mLastY = y
            }

            mLastPointerCount = pointerCount

            val rectF = getMatrixRectF()

            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    if (rectF.width() > width || rectF.height() > height) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (rectF.width() > width || rectF.height() > height) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    var dx = x - mLastX
                    var dy = y - mLastY

                    Log.d(TAG,"isCanDrag status: $mIsCanDrag")
                    if (!mIsCanDrag) {
                        mIsCanDrag = isCanDrag(dx, dy)
                    }
                    if (mIsCanDrag) {

                        if (drawable != null) {
                            isCheckLeftAndRight = true
                            isCheckTopAndBottom = true
                            // 如果宽度小于屏幕宽度，则禁止左右移动
                            if (rectF.width() < width) {
                                dx = 0f
                                isCheckLeftAndRight = false
                            }
                            // 如果高度小于屏幕高度，则禁止上下移动
                            if (rectF.height() < height) {
                                dy = 0f
                                isCheckTopAndBottom = false
                            }

                            //设置偏移量
                            mScaleMatrix.postTranslate(dx, dy)
                            //再次校验
                            checkMatrixBounds()
                            imageMatrix = mScaleMatrix
                        }
                    }
                }
            }
        }
        return true
    }

    private fun checkMatrixBounds() {
        val rectF = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f
        val viewWidth = width
        val viewHeight = height

        if (rectF.top > 0 && isCheckTopAndBottom){
            deltaY = -rectF.top
        }
        if (rectF.bottom < viewHeight && isCheckTopAndBottom){
            deltaY = viewHeight - rectF.bottom
        }

        if (rectF.left > 0 && isCheckLeftAndRight){
            deltaX = -rectF.left
        }
        if (rectF.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX,deltaY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    private fun isCanDrag(dx: Float, dy: Float): Boolean {
        return sqrt(((dx * dx) + (dy * dy))) > mTouchSlop
    }

    override fun onGlobalLayout() {
        if (mOnce){
            val drawable = drawable ?: return

            val width = width
            val height = height

            val dw = drawable.intrinsicWidth
            val dh = drawable.intrinsicHeight

            var scale = 1.0f

            if (dw < width && dh < height){
                scale = width.toFloat()/dw
            }

            if (dw > width && dh <= height){
                scale = width.toFloat() / dw
            }
            if (dh > height && dw <= width){
                scale = height.toFloat() / dh
            }

            if (dw > width && dh > height){
                scale = min(width.toFloat()/dw,height.toFloat()/dh)
            }
            mInitScale = scale
            mMaxScale = mInitScale * 3
            mMidScale = mInitScale * 1.5f

            mScaleMatrix.postTranslate((width - dw).toFloat()/2,(height-dh).toFloat()/2)
            mScaleMatrix.postScale(scale,scale,getWidth().toFloat()/2,getHeight().toFloat()/2)
            imageMatrix = mScaleMatrix
            mOnce = false
        }
    }

    private inner class AutoScaleRunnable public constructor(
        private val mTargetScale: Float,
        private val x: Float,
        private val y: Float
    ) : Runnable{
        private val mTmpScale:Float = if(getScale()< mTargetScale){
            BIGGER
                }else{
            SMALLER
                }

        override fun run() {
            mScaleMatrix.postScale(mTmpScale,mTmpScale,x,y)
            checkBorderAndCenterWhenScale()
            imageMatrix = mScaleMatrix

            val currentScale:Float = getScale()

            if (((mTmpScale > 1f) and (currentScale < mTargetScale))
                || ((mTmpScale < 1f) and (mTargetScale < currentScale))){
                postDelayed(this,16)
            }else{
                val deltaScale = mTargetScale / currentScale
                mScaleMatrix.postScale(deltaScale,deltaScale,x,y)
                checkBorderAndCenterWhenScale()
                imageMatrix = mScaleMatrix
                mIsAutoScale = false
            }
        }

    }

    private fun checkBorderAndCenterWhenScale() {
        val rect = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f

        val width = width
        val height = height

        if (rect.width() >= width){
            if (rect.left > 0){
                deltaX = (-rect.left)
            }
            if (rect.right < width){
                deltaX = (width - rect.right)
            }
        }

        if (rect.height() >= height){
            if (rect.top >= 0){
                deltaY = (-rect.top)
            }

            if (rect.bottom < height){
                deltaY = (height - rect.bottom)
            }
        }
        if (rect.width() < width){
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width()
        }
        if (rect.height() < height){
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height()
        }

        mScaleMatrix.postTranslate(deltaX,deltaY)
    }

    private fun getMatrixRectF(): RectF {
        val matrix = mScaleMatrix
        val rect:RectF = RectF()
        val drawable = drawable
        if (drawable != null){
            rect.set(0f,0f,drawable.intrinsicWidth.toFloat(),drawable.intrinsicHeight.toFloat())
            matrix.mapRect(rect)
        }

        return rect
    }

    companion object{
        private const val TAG = "ScaleImageView"

        const val BIGGER = 1.07f
        const val SMALLER = 0.93f
    }
}