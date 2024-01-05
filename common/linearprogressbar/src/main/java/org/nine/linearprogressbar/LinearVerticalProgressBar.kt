package org.nine.linearprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt


class LinearVerticalProgressBar : BaseProgressBar {

    private var mHeight = 0

    constructor(context: Context) : super(context)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun callInit() {
        super.callInit()
        init()
    }

    private fun init() {
        paintBackground.apply {
            color = mBackground
            style = Paint.Style.FILL
        }
        textPaint.apply {
            color = mTextColor
            textSize = mTextSize
            if (mFontFamily != 0) {
                try {
                    val tp = ResourcesCompat.getFont(context, mFontFamily)
                    this.typeface = Typeface.create(tp, Typeface.NORMAL)
                } catch (e: Exception) {
                    Log.e("LinearVerticalProgressBar", e.message.toString())
                    e.printStackTrace()
                    throw e
                }
            }
        }
        if (mGradient) {
            paint.apply {
                shader = LinearGradient(
                    0f,
                    0f,
                    0f,
                    mHeight.toFloat(),
                    mGradientStartColor,
                    mGradientEndColor,
                    Shader.TileMode.CLAMP
                )
            }
        } else {
            paint.apply {
                color = mProgressColor
                style = Paint.Style.FILL
            }
        }

        progressRect =
            RectF(0f, (mHeight - ((mProgress * mHeight) / 100f)), mThickness, mHeight.toFloat())
        backgroundRect = RectF(0f, 0f, mThickness, mHeight.toFloat())

        setProgress(mProgress)
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        val to = (mHeight - ((mProgress * mHeight) / 100f))
        val animator = ValueAnimator.ofFloat(progressRect.bottom, to)
        animator.duration = mDuration.toLong()
        animator.addUpdateListener {
            progressRect = RectF(
                progressRect.left,
                it.animatedValue.toString().toFloat(),
                progressRect.right,
                progressRect.bottom
            )
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRoundRect(
            backgroundRect, mRadius, mRadius, paintBackground
        )
        canvas.drawRoundRect(
            progressRect, mRadius, mRadius, paint
        )

        var text = (100 - (progressRect.top * 100 / mHeight)).toString()
        text = text.toFloat().roundToInt().toString()

        textPaint.getTextBounds(text, 0, text.length, textBound)

        val y = if (progressRect.bottom - progressRect.top < textBound.height()) {
            ((progressRect.bottom - textBound.height() / 2))
        } else if (backgroundRect.height() - progressRect.height() < textBound.height()) {
            ((progressRect.top + textBound.height()))
        } else progressRect.top + (textBound.height() / 2f)

        if (mTextVisibility == VISIBLE) {
            canvas.drawText(
                text.toInt().toString(),
                (backgroundRect.right.toInt()).toFloat() + textBound.right / 2f,
                y,
                textPaint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        textPaint.getTextBounds("H", 0, 1, textBound)

        val boundHeight = if (mTextVisibility == GONE) 0 else textBound.height()


        val desiredHeight = backgroundRect.bottom.toInt()
        val desiredWidth = (backgroundRect.right.toInt()) + boundHeight * 3

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }

            MeasureSpec.AT_MOST -> {
                desiredWidth.coerceAtMost(widthSize)
            }

            else -> {
                desiredWidth
            }
        }
        mHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }

            MeasureSpec.AT_MOST -> {
                desiredHeight
            }

            else -> {
                desiredHeight
            }
        }

        init()
        setMeasuredDimension(width, mHeight)

    }
}