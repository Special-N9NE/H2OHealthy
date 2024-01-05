package org.nine.linearprogressbar

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat


open class BaseProgressBar : View , InitListener{

    protected val textBound = Rect()

    protected var mProgress = 0
    protected var mBackground = Color.GRAY
    protected var mProgressColor = Color.CYAN
    protected var mGradient = false
    protected var mGradientStartColor = Color.CYAN
    protected var mGradientEndColor = Color.CYAN
    protected var mTextColor = Color.WHITE
    protected var mTextSize = 40f
    protected var mThickness = 25f
    protected var mRadius = 20f
    protected var mDuration = 2000
    protected var mTextVisibility = VISIBLE
    protected var mFontFamily = 0

    protected var progressRect = RectF()
    protected var backgroundRect = RectF()

    protected val paint = Paint()
    protected val paintBackground = Paint()
    protected val textPaint = Paint()

    constructor(context: Context) : super(context)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseProgressBar)
        mProgress = a.getInt(R.styleable.BaseProgressBar_progressValue, 0)

        mProgressColor =
            a.getColor(R.styleable.BaseProgressBar_progressColor, mProgressColor)
        mBackground =
            a.getColor(R.styleable.BaseProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.BaseProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(
                R.styleable.BaseProgressBar_textSize,
                mTextSize.toInt()
            )
                .toFloat()
        mThickness = a.getDimensionPixelSize(
            R.styleable.BaseProgressBar_thickness, mThickness.toInt()
        ).toFloat()
        mRadius = a.getDimensionPixelSize(
            R.styleable.BaseProgressBar_radius, mRadius.toInt()
        ).toFloat()
        mDuration = a.getInt(R.styleable.BaseProgressBar_animationDuration, mDuration)
        mTextVisibility =
            a.getInt(R.styleable.BaseProgressBar_textVisibility, mTextVisibility)
        mFontFamily = a.getResourceId(R.styleable.BaseProgressBar_font, 0)
        mGradient = a.getBoolean(R.styleable.BaseProgressBar_gradient, false)
        mGradientStartColor =
            a.getColor(
                R.styleable.BaseProgressBar_gradientStartColor,
                mGradientStartColor
            )
        mGradientEndColor =
            a.getColor(R.styleable.BaseProgressBar_gradientEndColor, mGradientEndColor)

        a.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val a =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.BaseProgressBar,
                defStyleAttr,
                0
            )

        mProgressColor =
            a.getColor(R.styleable.BaseProgressBar_progressColor, mProgressColor)
        mBackground =
            a.getColor(R.styleable.BaseProgressBar_progressBackgroundColor, mBackground)
        mTextColor = a.getColor(R.styleable.BaseProgressBar_textColor, mTextColor)
        mTextSize =
            a.getDimensionPixelSize(
                R.styleable.BaseProgressBar_textSize,
                mTextSize.toInt()
            )
                .toFloat()
        mThickness = a.getDimensionPixelSize(
            R.styleable.BaseProgressBar_thickness, mThickness.toInt()
        ).toFloat()
        mRadius = a.getDimensionPixelSize(
            R.styleable.BaseProgressBar_radius, mRadius.toInt()
        ).toFloat()
        mDuration = a.getInt(R.styleable.BaseProgressBar_animationDuration, mDuration)
        mTextVisibility =
            a.getInt(R.styleable.BaseProgressBar_textVisibility, mTextVisibility)
        mFontFamily = a.getResourceId(R.styleable.BaseProgressBar_font, 0)
        mGradient = a.getBoolean(R.styleable.BaseProgressBar_gradient, false)
        mGradientStartColor =
            a.getColor(
                R.styleable.BaseProgressBar_gradientStartColor,
                mGradientStartColor
            )
        mGradientEndColor =
            a.getColor(R.styleable.BaseProgressBar_gradientEndColor, mGradientEndColor)

        a.recycle()
    }
    open fun setProgress(progress: Int) {
        mProgress = progress
    }
    fun setProgressBackgroundColor(color: Int) {
        mBackground = color
        this.callInit()
    }

    fun setProgressColor(color: Int) {
        mProgressColor = color
        this.callInit()
    }

    fun setTextColor(color: Int) {
        mTextColor = color
        this.callInit()
    }

    fun setAnimationDuration(duration: Int) {
        mDuration = duration
    }

    fun setTextVisibility(visibility: Int) {
        if (visibility in listOf(VISIBLE, INVISIBLE, GONE)) mTextVisibility = visibility
    }

    fun setFont(resourceId: Int) {
        try {
            ResourcesCompat.getFont(context, resourceId)
            mFontFamily = resourceId
            this.callInit()
        } catch (e: Exception) {
            Log.e("LinearProgressBar", e.message.toString())
            e.printStackTrace()
            throw e
        }
    }

    fun getProgress(): Int {
        return mProgress
    }

    fun getProgressBackgroundColor(): Int {
        return mBackground
    }

    fun getProgressColor(): Int {
        return mProgressColor
    }

    fun getTextColor(): Int {
        return mTextColor
    }

    fun getThickness(): Float {
        return mThickness
    }

    fun getRadius(): Float {
        return mRadius
    }

    fun getAnimationDuration(): Int {
        return mDuration
    }

    fun getTextVisibility(): Int {
        return mTextVisibility
    }

    override fun callInit() {}
}